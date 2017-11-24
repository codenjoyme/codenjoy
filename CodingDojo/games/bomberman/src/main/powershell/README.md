## PowershellCodenjoyBot
Powershell helper/client module for 
[CodingDojo project](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/) **Bomberman game**.  
*Prerequisites: .NET Framework since 4.5*

[Bobmberman game rules.](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/games/bomberman/src/main/webapp/resources/help/bomberman.html)

Powershell WebSockets implementation based on the following:  
https://github.com/markwragg/Powershell-SlackBot  
https://github.com/brianddk/ripple-ps-websocket  

## Quickstart

1. [Build/Run local gameserver](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/) and [Register your player](http://127.0.0.1:8080/codenjoy-contest/register)

2. Import **`BombermanAPI.psm1`** module into your PS session to access basic/helper functions  
(specify full path to the .psm1 module if module location differs from shell location )
```powershell
Import-Module .\BombermanAPI.psm1 -Force
```

3. Change/Set your Gameserver websocket connection URI and your Username in the **`$Global:BombermanURI`** variable
```powershell
[URI]$Global:BombermanURI = "ws://127.0.0.1:8080/codenjoy-contest/ws?user=username@users.org"
```

3. Execute a loop below. Your Bomber will start moving randomly every 1sec.
```powershell
while ($true)
{
	move $(Random("act", "left", "right", "up", "down"))
}
```

## How to play

* Gameserver constantly sends string gameboard describing current game situation, BoardSize is 33x33 points, therefore you will get a string of 33x33+6(prefix)=1095 UFT8 characters every tick(second). Here is how it looks like:  

**board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼        #   # #               #☼☼ ☼ ☼ ☼ ☼ ☼#☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼              ##     ♥     ### ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼&☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼                #        #     ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼☼                           # ##☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼&                             #☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼                               ☼☼ ☼ ☼ ☼ ☼ ☼ ☼&☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼#☼☼                 #       ##    ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼                        # #    ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼#☼ ☼☼    &           #    #       # ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼☼                     #    #  # ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼         ##  #          ## ## #☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼#☼#☼ ☼ ☼☼##        &       # #  ##   #  ☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼☼###  #  #         #    # ###   ☼☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼#☼ ☼☼# # # #       # #♥##   # ####  ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼#☼#☼☺☼ ☼ ☼☼&   #  #      #   &  ## ###   &☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼    #     #     # #     # & # &☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼**  

 ### Basics function usage/how-to

* **`Invoke-GameSync`** is a core function in order to play the game.  
**`Invoke-GameSync`** functions execution takes a minimum of 1 second (1 game turn).   
By default, `-NextAction` parameter value is `"wait"`, therefore by calling function w/o `-NextAction` parameter you will make no action except gameboard synchronization.    
To make a move(and/or act) specify this in the `-NextAction` parameter
```powershell
Invoke-GameSync -NextAction 'act, down'
```
or pipe your action to **`Invoke-GameSync`**
```powershell
'act, up' | Invoke-GameSync
```
or use **`move`** alias for **`Invoke-GameSync`** command
```powershell
move "up"
move "left, act"
```

* To view gameboard in console output use **`Show-GameBoardRawGrid`**
```powershell
Invoke-GameSync
Show-GameBoardRawGrid
```

* To view gameboard in console output continiously use ininite loop
```powershell
while ($true)
{
	Clear-Host
	Invoke-GameSync
	Show-GameBoardRawGrid
}
```

* Anytime when you call the **`Invoke-GameSync`** function, it updates **`[string]$Global:CurrentGameBoardRawString`** variable with current gameboard string   
Call/Use **`[string]$Global:CurrentGameBoardRawString`** if you need to access raw gameboard string  
For instance, you can store current gameboard string into your variable and use that string with another function later
```powershell
$myGameString = $Global:CurrentGameBoardRawString
Show-GameBoardCharArray -GameBoardRawString $myGameString
Show-GameBoardCharArray($myGameString)
Show-GameBoardCharArray $myGameString
$myGameString | Show-GameBoardCharArray
```

* **`Get-GameBoardElementArray`** gives you two-dimensional array which represents gameboard and contains game CHARS 
```powershell
Invoke-GameSync 
$GameBoard = Get-GameBoardCharArray
```

* **`Get-GameBoardElementArray`** function gives you a two-dimensional array which represents gameboard and contains game ELEMENTS
**`Get-GameBoardElementArray`** is a core funtion to analyze gameboard field.
Refer to [x,y] index to get/check any element within gameboard
```powershell
Invoke-GameSync 
$GameBoard = Get-GameBoardElementArray
$GameBoard[15,16]
```

* **`Get-GameElementCollection`** is a core funtion to analyze game elements collections. Use `-Element` parameter to specify target list/collection    
Below example shows how to get a count of walls that exist on the game field all coordinates of these walls
```powershell
Invoke-GameSync
$AllWalls = Get-GameElementCollection -Element Wall
$AllWalls.Count
```

* If you need to get a single element from a given collection, use the first index
```powershell
Invoke-GameSync
$badGuys = Get-GameElementCollection -Element OtherBomberman
$badGuys.Count
$badGuys[0]
```

* To obtain X,Y points, refer to the second index
```powershell
Invoke-GameSync
$MeatChopper = Get-GameElementCollection -Element MeatChopper
$MeatChopperXcoordinate = $MeatChopper[2][0]
$MeatChopperYcoordinate = $MeatChopper[2][1]
```


### Sample algorithm on the top of basic functions.  
Place a bomb then move to any free space.

```powershell
region START

# gametime counter represents relative game time during infinite loop cycle execution
$GameTime = 5
# this is how algorithm remembers last time when bomb was placed
$LastTimeBombPlaced = 0
# just define var to store next move
$myNextAction = "wait"

# infinite loop. game is running as long as powershell session exists
while ($true)
{
	# gametime counter will be increased by 1 every turn
	$GameTime++
	"`n NEW GameTime is " + $GameTime + " LastTimeBombPlaced was " + $LastTimeBombPlaced
	
	# sync the game. Send your game decision by specifying -NextAction , get current gameboard in background
	# at a first loop iteration -NextAction will be "wait", after that $myNextAction will be changed continuously depending on game situation analyze 
	Invoke-GameSync -NextAction $myNextAction
	
	# store gameboard element array into $GameBoard variable
	$GameBoard = Get-GameBoardElementArray
	
	# Get Bomber's position
	# If bomb just been placed, Bomberman is 'BombBomberman' game element
	$myBombBomber = Get-GameElementCollection -Element BombBomberman
	If ($myBombBomber)
	{
		$x = $myBombBomber[0][0]
		$y = $myBombBomber[0][1]
	}
	# In general case Bomberman is 'Bomberman' game element
	$myBomber = Get-GameElementCollection -Element Bomberman
	If ($myBomber)
	{
		$x = $myBomber[0][0]
		$y = $myBomber[0][1]
	}
	
	"Bomber at x=$($x) y=$($y)"
		
	
	# Place a bomb if has not been placed during last 5 game turns  
	If (($LastTimeBombPlaced + 5) -lt $GameTime)
	{
		# set the action 
		$myNextAction = "act"
		# store gametime of bombplace action 
		$LastTimeBombPlaced = $GameTime
		"Placing BOMB" + " at GameTime " + $GameTime
		# Ending current loop iteration because -$myNextAction has just been choosen
		Continue
	}
	
	# If not able to place a bomb at current game turn, need to make a move.
	# Check gameboard cells near bomberman's position . First "space" cell will be choosen for the next move.
	if ($GameBoard[($x+1),($y)] -match "Space")
	{
		$myNextAction = "right"
		"Moving RIGHT" + " at GameTime " + $GameTime
		Continue
	}
	if ($GameBoard[($x-1),($y)] -match "Space")
	{
		$myNextAction = "left"
		"Moving LEFT" + " at GameTime " + $GameTime
		Continue
	}
	if ($GameBoard[($x),($y+1)] -match "Space")
	{
		$myNextAction = "up"
		"Moving UP" + " at GameTime " + $GameTime
		Continue
	}
	if ($GameBoard[($x),($y-1)] -match "Space")
	{
		$myNextAction = "down"
		"Moving DOWN" + " at GameTime " + $GameTime
		Continue
	}

}
#endregion END
```


### Helper function usage/how-to

* позиция моего бомбера на доске  
`Point getBomberman()`
```powershell
getBomberman
```

* позиции всех остальных бомберов (противников) на доске  
`Collection<Point> getOtherBombermans()`
```powershell
getOtherBombermans
```

* жив ли мой бомбер
`boolean isMyBombermanDead()`
```powershell
isMyBombermanDead
```
* находится ли в позиции  x, y заданный элемент? находится ли в позиции  x, y что-нибудь из заданного набора  
`boolean isAt(int x, int y, Element element)`
`boolean isAt(int x, int y, Collection<Element> elements)`
```powershell
isAt -X 32 -Y 15 -Element MeatChopper
isAt 32 15 Space,Boom,BombTimer1,Wall
```
* есть ли вокруг клеточки с координатой x,y заданный элемент
`boolean isNear(int x, int y, Element element)`
```powershell
isNear -x 29 -y 31 -Element MeatChopper
```
* есть ли препятствие в клеточке x, y
`boolean isBarrierAt(int x, int y)` 
```powershell
isBarrierAt -X 32 -Y 32
```
* сколько элементов заданного типа есть вокруг клетки с x, y
`int countNear(int x, int y, Element element)`
```powershell
countNear -X 16 -Y 15 -Element Wall
```
* возвращает элемент в текущей клетке
`Element getAt(int x, int y)`
```powershell
getAt -X 15 -Y 16
```

* возвращает размер доски
`int boardSize()`
```powershell
boardSize
```

* координаты всех объектов препятствующих движению
`Collection<Point> getBarriers() `
```powershell
getBarriers
```

* координаты всех чудиков которые могут убить бомбера
`Collection<Point> getMeatChoppers()`
```powershell
getMeatChoppers
```

* координаты всех бетонных стен
`Collection<Point> getWalls()`
```powershell
getWalls
```

* координаты всех кирпичных стен (их можно разрушать)
`Collection<Point> getDestroyWalls()`
```powershell
getDestroyWalls
```

* Координаты всех бомб. 
`Collection<Point> getBombs()`
```powershell
getBombs
```

* координаты потенциально опасных мест, где бомба может разорваться.
`Collection<Point> getFutureBlasts()`
```powershell
getFutureBlasts
```

* converts index within raw gamestring into gameboard x,y points 
```powershell
strpos2xy
```

* converts gameboard x,y points into index within raw gamestring
```powershell
xy2strpos
```

