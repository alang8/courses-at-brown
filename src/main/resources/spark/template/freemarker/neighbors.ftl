<#assign neighbors>

<p>
<form method="POST" action="/neighbors">
<label for="xval" style="color: white;"> Choose target by coordinate </label><br>
<textarea name="xval" id="x-value" rows = "1" placeholder = "x-coordinate"></textarea><br>


<label for="yval"> </label><br>
<textarea name="yval" id="y-value" rows = "1" placeholder = "y-coordinate"></textarea><br>


<label for="zval"> </label><br>
<textarea name="zval" id="z-value" rows = "1" placeholder = "z-coordinate"></textarea><br>

<br>
<label for="starName" style="color: white;" >Or, choose target by name </label><br>
<textarea name="starName" id="starNameNeighbors" rows = "1" placeholder = "target star name"></textarea><br>

<br>
<label for="numNeighbors" style="color: white;">Desired # of results</label><br>
<textarea name="numNeighbors" id="numNeighbors" rows = "1" placeholder = "number of neighbors"></textarea><br>

<br>
<input type="radio" name="whichAlgo" value="naive" id = "naive"/>
<label for="naive" style="color: white;">Naive search algorithm</label>
<br>
<input type="radio" name="whichAlgo" value="advanced" id = "advanced"/>
<label for="naive" style="color: white;">Advanced search algorithm</label>
<br><br>
<input type="submit">

<br>
</form>
</p>

</#assign>

<#include "main.ftl">
