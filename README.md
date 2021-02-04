# iTunesDateChanger
Short Java application to change the audio files "added date"

> Background story: as a subscriber of [iTunes Match](https://support.apple.com/en-us/HT204146) you can constantly ran into different issues. In this particular case, I like my music library organised by the date I (let's say) "discovered" the given artis / song. The only way I can do this is that I sort the songs by "Date Modified". However on iPhone this configuration is not present. It sorts the library by "Date Added" which can differ from the "Date Modified". 

The main purpose of this application (in steps) is to:
 1. the user inputs a full path to a directory
 2. it maps the given folder by searching for audio files _(.m4a, .mp3, .wav, .aif, .wma)_
 3. extract the "last modification date" from the files
 4. set the system time according to this date & time
 5. add the audio file into iTunes

The application uses **Java** but there are also Windows command prompt commands used throught **cmd.exe** and **powershell.exe**.
