This is a tech demo of my game. There is one player and one level currently. The
demo ends once you reach the top of the map and have defeated all enemies.

Running the game:
First of all, the game should not be started from a slow USB stick. Copy the jar
file to an internal HDD.

1) Make sure the Java Runtime Environment 1.7+ is installed.
2) Run the jar file.
3) Choose your preferred language.
4) Click on options and change them as desired.
5) Click start to run the game. 

Controls:

- Arrow keys: Movement
- Left shift: Faster movement, skip textboxes
- Page up / Q: Switch weapon.
- Insert / W: Switch special weapon.
- Page down / Forward del or A/S: Switch targeted enemy to next/previous enemy.
- Enter: Close and proceed to the next text box.
- Escape: Pause game, hold down escape while paused to exit the game. Press
any other button to resume the game.


Running the game from the command line:
You can run the application directly from the command line with the command
below.  This will print debugging and error information to stdout / stderr:

> $java -cp lotsOfBS.jar de.homelab.madgaksha.desktop.DesktopLauncher


Source Code:
This project's source code can be cloned from github:
> $ git clone https://github.com/blutorange/lotsOfBSGdx

The main game's source coded is located in:
> ./lotsOfBSGdx/core/src/de/homelab/madgaksha

Desktop-specific code for launching the game can be found at:
> ./lotsOfBSGdx/desktop/src/de/homelab/madgaksha

Compilation:
This project uses libGDX and libGDX uses gradle to be independent from any
specific IDE / environment. See the instruction on the homepage of libGDX for
further instructions. In short, you will have to proceed as follows:

(1) Setup your development environment.

https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29

(2) Clone the project

> $ git clone https://github.com/blutorange/lotsOfBSGdx

(3) Import and run the project

Eclipse: https://github.com/libgdx/libgdx/wiki/Gradle-and-Eclipse

Intellij IDEA: https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA

NetBeans: https://github.com/libgdx/libgdx/wiki/Gradle-and-NetBeans

Command Line: https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline

(4) Package the project

https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline#packaging-the-project 
