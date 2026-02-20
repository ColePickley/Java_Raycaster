# Java_Raycaster
Welcome to my raycaster program!
If you don't know what a raycaster is, put simply, it's a 3D simulation of a 2D space.
This simulation is accomplished by sending rays out from the player's position and 
detecting the length of those rays when they hit a wall. Those rays are then translated 
to vertical lines on the screen.

To help users understand what's going on, I've creates a window with a birds-eye
view of the map, player, and rays along side the normal game window.

**In order to use this program the way it is intended to be used, you should only have to modify variables in the Run.py file**

You can create maps using 2D arrays for the raycaster to process. I've left a few
examples for you to try, but feel free to make your own. In order to run the program, 
you do need to create a Player object with a specified rotation and starting coordinates
on the map grid. These coordinates start at x = 0, y = 0 in the top left corner.
