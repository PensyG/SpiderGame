# SpiderGame
Game created for an intro programming class

Game Objective:
Navigate the web as a spider with a limited view distance. Flies will randomly get stuck in your web, and vibrate.
You can feel the vibration around yourself, and your goal is to find and eat flies to stay alive. The flies have a
chance to escape each turn, and after a long enough time in the web, can die.

Input:
An 2 character max combination input of the four cardinal directions mapped to WASD allows you to move in 8 directions.
In addition, you have a certain distance you move in any of those directions each turn, based on the size of the web.
Separate the two with a space, and you have a valid input: 'wd 5', or 'a 7'. 
Note: The input must have the direction first, and the distance second, so '3 ds' won't work, but 'ds 3' would
