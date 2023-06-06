package core;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagException;
import helpers.ExtractDateTimeHelper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static consoleUI.InputFromWin.*;

public class CollectorOnWin extends Collector {

    private ExtractDateTimeHelper dateTimeHelper = new ExtractDateTimeHelper();
    private List<AudioTrack> playlistAudioTracks = new ArrayList<>();
    private List<File> listOfFiles = new ArrayList<>();
    private final String DATE_STRING = "yyyy-MM-dd";
    private boolean YES2ALL = false;
    private boolean NO2ALL = false;

    // Recursively collecting audio files that can be found in the provided path.
    public void collectFiles(String userPath) {
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));

        // If the provided path is a single file, it is added straight to the array
        // otherwise and array of found files is created for further conversion.
        if (input.isFile()) {
            listOfFiles.add(input);
        } else if (input.isDirectory()){
            for (File file : input.listFiles()) {
                listOfFiles.add(file);
            }
        }

        String iTunesAddSongCommand = "$itunes.LibraryPlaylist.addFile";
        String iTunesConvertCommand = "$itunes.ConvertFile";

        for (File file : listOfFiles) {
                String fileName = file.getName();
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wav)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(fileName);
                if (file.isFile()) {
                    if (matcher.matches()) {
                        String filePath = "(\\\"" + file + "\\\"); ";
                        String[] fileDateTime = dateTimeHelper.extractOnWin(file + "");
                        String fileDate = "set-date -date '" + fileDateTime[0].replace("-","/")
                                + " " + fileDateTime[1] + "'; ";

                        supportedAudioFiles.add(fileDate + iTunesAddSongCommand + filePath);
                        sumOfFiles++;
                    } else if (fileName.matches(".+?\\.(wma|WMA)$")) {
                        String filePath = "(\"" + file + "\")";
                        String[] fileDateTime = dateTimeHelper.extractOnWin(file + "");
                        String fileDate = "set-date -date '" + fileDateTime[0].replace("-","/")
                                + " " + fileDateTime[1] + "'; ";

                        convertibleAudioFiles.add(fileDate + iTunesConvertCommand + filePath);
                        sumOfFiles++;
                    } else if (fileName.matches(".+?\\.(txt|TXT)$")) {
                        System.out.println("Provided file is an iTunes playlist export. \nScanning for tracks ...");
                        scanPlaylist(input);
                        if (playlistAudioTracks.size() > 0) {
                            System.out.println("Number of songs in playlist: " + playlistAudioTracks.size()+
                                    "\nProvide a directory to search for tracks: ");
                            try {
                                File directory = getDirectory();
                                searchDirectory(directory);
                            } catch (IOException | TagException | CannotReadException | InvalidAudioFrameException exception) {
                                System.err.println(exception);
                            }

                            System.out.println("Found: " + listOfFiles.size() + " of " + playlistAudioTracks.size());
                            getMissingTracks();
                            collectFiles("");
                        } else {
                            System.out.println("No tracks found in playlist ...");
                            break;
                        }
                    }
                } else if (file.isDirectory()) {
                    collectFiles(userPath + "\\" + fileName);
                }
        }
        System.out.println("Number of compatible audio files: " + sumOfFiles +
                "\nNumber of audio files (.wma) needs conversion: " + convertibleAudioFiles.size());
    }

    public void scanPlaylist(File input) {
        try {
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()){
                String[] trackTags = scanner.nextLine().split("\t");
                String trackTitle = trackTags[0].trim();
                String trackArtist = trackTags[1].trim();
                String trackDate = trackTags[17].replaceAll("\s.*", "").trim();

                SimpleDateFormat formatter = new SimpleDateFormat(DATE_STRING);
                Date dateModified = null;

                try {
                    dateModified = formatter.parse(trackDate);
                } catch (ParseException exception) {
                    System.err.println(exception);
                }

                if (dateModified != null) {
                    AudioTrack audioTrack = new AudioTrack(trackTitle, trackArtist);
                    audioTrack.setDateModified(dateModified);
                    playlistAudioTracks.add(audioTrack);
                }
            }
        } catch (FileNotFoundException exception) {
            System.err.println(exception);
        }
    }

    public File getDirectory() throws IOException {
        String mediaPath = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            mediaPath = input.readLine().trim();
        } catch (IOException exception) {
            System.err.println(exception);
        }

        File userInput = new File(mediaPath.replaceAll("(\\\\|/)$", ""));
        if (!userInput.isDirectory()) {
            System.out.println("Please provide a valid directory path: ");
            getDirectory();
        }

        return userInput;
    }

    // Recursively searches through the given directory while comparing each file with the playlist tracks
    private void searchDirectory(File userInput)
            throws IOException, TagException, CannotReadException, InvalidAudioFrameException {
        System.out.println("Searching in folder: " + userInput.getName());

        for (File file : userInput.listFiles()) {
            Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wav|wma|WMA)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(file.getName());

            if (file.isFile() && matcher.matches()) {
                String[] fileDateTime = dateTimeHelper.extractOnWin(file + "");
                Date audioFileDate = null;
                String audioFileTitle = "";
                String audioFileArtist = "";

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(DATE_STRING);
                    audioFileDate = formatter.parse(fileDateTime[0]);
                    audioFileTitle = getAudioFileTag(file, true);
                    audioFileArtist = getAudioFileTag(file, false);
                } catch (ParseException exception) {
                    System.err.println(exception);
                }

                // Compare files in directory with playlist tracks
                for (AudioTrack track : playlistAudioTracks) {
                    if (!audioFileArtist.equals(track.artist) || !audioFileTitle.equals(track.title)) {
                        continue;
                    }

                    System.out.println("Audio file found!\n" + audioFileTitle + " : " + audioFileArtist);
                    if (audioFileDate.compareTo(track.dateModified) != 0) {
                        System.err.println("Date of modification doesn't match!"
                                + "\n- Audio file: " + audioFileDate
                                + "\n- Playlist track: " + track.dateModified);
                        try {
                            getUserInput(file);
                        } catch (IOException exception) {
                            System.err.println(exception);
                        }
                    } else {
                        System.out.println("Adding found audio file to iTunes ...");
                        listOfFiles.add(file);
                    }
                }
            } else if (file.isDirectory()) {
                searchDirectory(file);
            }
        }
    }

    // Gets the metadata of the audio file
    private String getAudioFileTag(File file, boolean search4Title) {
        AudioFile audioFile = null;
        try {
            audioFile = AudioFileIO.read(file);
        } catch (IOException | CannotReadException | TagException | InvalidAudioFrameException exception) {
            System.err.println(exception);;
        }

        if (audioFile != null) {
            Tag fileTag = audioFile.getTag().or(NullTag.INSTANCE);
            return search4Title ?
                    fileTag.getValue(FieldKey.TITLE).or(""):
                    fileTag.getValue(FieldKey.ARTIST).or("");
        }
        return "";
    }

    private void getUserInput(File file) throws IOException {
        String userChoice = "";

        if (!YES2ALL && !NO2ALL) {
            System.out.println( "Add track anyway? (YES/NO/YES TO ALL /NO TO ALL)");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            userChoice = input.readLine().trim();
        }

        if (NO2ALL || userChoice.matches("NO|no|N|n")) {
            return;
        } else if (YES2ALL || userChoice.matches("YES|yes|Y|y")) {
            System.out.println("Adding found audio file to iTunes ...");
            listOfFiles.add(file);
        } else if (userChoice.matches("(YES TO ALL)|(yes to all)")) {
            YES2ALL = true;
        } else if (userChoice.matches("(NO TO ALL)|(no to all)")) {
            NO2ALL = true;
        } else {
            System.out.println("Please use \"YES \\ Y \", \"NO \\ N \", \"YES TO ALL\", \"NO TO ALL\" as an answer.");
            getUserInput(file);
        }
    }

    public void getMissingTracks() {
        System.out.println("Still missing tracks: ");

        List<String> fileList =
                listOfFiles.stream().map(track -> getAudioFileTag(track, true)).collect(Collectors.toList());
        playlistAudioTracks.stream().forEach(audioTrack -> {
            if (!fileList.contains(audioTrack.title)) {
                System.out.println(audioTrack.artist + " " + audioTrack.title + " " + audioTrack.dateModified);
            }
        });
    }

    // Object to store tacks from playlist
    public static class AudioTrack {
        private String title;
        private String artist;
        private Date dateModified;

        public AudioTrack(String title, String artis) {
            this.title = title;
            this.artist = artis;
        }

        public void setDateModified(Date dateModified) {
            this.dateModified = dateModified;
        }
    }
}