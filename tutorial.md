# How to kill the emulators?
* Find the location of the Android SDK
1. File -> New Projects Setup -> Default Project structure
2. Copy the address in the section of Android SDK location

* set up `adb`
1. Input `adb`, if `adb: command not found`, you have to set up.
2. Open the terminal
3. Input `echo $HOME`
4. Create `.bash_profile` file by input `touch .bash_profile`
5. Open `.bash_profile` file by inputting `open -e .bash_profile`
6. In this file, enter `export PATH=${PATH}:/Users/XXX/Library/Android/sdk/platform-tools:/Users/XXX/Library/Android/sdk/tools`
## XXX should be replaced by the proper name of your address.
7. Closing `.bash_profile` file will automatically save it.
8. Verify whether it's set up successfully by enter `source .bash_profile`
9. Enter `adb`, if there is no `-bash: adb: command not found`, then we are successful.

* close the emulator!
* enter `adb emu kill` to kill one emulator
* if you have more than one emulator
1. enter `adb devices` to find out the serial numbers of the running emulators
2. `adb -s <serial-number> emu kill` to kill specific emulator
3. for example, `adb -s emulator-5554 emu kill`