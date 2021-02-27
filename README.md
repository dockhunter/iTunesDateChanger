# iTunesDateChanger
Short Java application to change the audio file's "added date" in iTunes

> Background story: as a subscriber of [iTunes Match](https://support.apple.com/en-us/HT204146) you can constantly ran into different issues with your beloved music library (missing cover arts, syncing issues, files not matching & uploading, etc...). In my case, I like my music library in my Music app (formerly called iTunes) organised by (let's say) "the date I discovered" the given artis or song. The only way I can do this is, by sorting the songs by "Date Modified". However (as I am using iTunes Match) on my iPhone, this configuration is not present. The music app sorts the library by "Date Added" by default, this means, that the recently added song will show up as first in my phone's libray. This can, ofcourse, differ from the file's modification date, as I had my music library way before I started using iTunes. 

The main purpose of this application (in steps) is to:
 1. the user inputs a full path to a directory
 2. it maps the given folder by searching for audio files _(.m4a, .mp3, .wav, .aif, .wma)_
 3. extract the "last modification date" from the files
 4. set the system time according to this date & time
 5. add the audio file into iTunes
 6. restores the system time and date back to normal.

The application is a maven project that uses **Java** but there are also functions that execute command prompt and powershell commands throught **cmd.exe** and **powershell.exe**.

Update 2021.02.06: Dependencie added for profesorfalken jPowerShell  

Update 2021.02.24: Dependencie added for tongFei Progressbar 0.9.0
