# SpiderGame
Game created for an intro programming class

Game Objective:
You are a spider '*' in a web with a limited view distance (dependant on difficulty).
Flies have gotten stuck in your web. It's your job to navigate the web, and eat them
to survive. In order to find them, you must look at the current values in your view.
Each value represents a vibration amount given off by a fly. The closer the value is
to a fly, the higher the value. Larger flies have larger amounts of vibration and can
be felt from farther away. As you survive, fewer flies will get stuck in your web.
Survive as long as possible and get the highest score.

Input:
A 2 character max combination input of the four cardinal directions mapped to 'WASD'
UP = 'w', DOWN = 's', RIGHT = 'd', LEFT = 'A'. This allows you to move in 8 directions.
In addition, you can only move 5 elements each turn. You can't input opposite
directions (RIGHT-LEFT, UP-DOWN) or the same direction twice (UP-UP). It must also
be in 'Direction - Distance' format, 'Distance - Direction' won't work.
Valid: 'wd 5', 'a 1', 's 5'
Invalid: 'ss 3', 'ad 2', 'a 6', '3 sd'
