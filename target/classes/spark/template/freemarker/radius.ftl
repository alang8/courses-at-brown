<#assign radius>

<p>
<form method="POST" action="/radius">
<label for="xval" style="color: white;"> Choose target by coordinate</label><br>
<textarea name="xval" id="xval" rows = "1" placeholder = "x-coordinate"></textarea><br>

<label for="yval"></label><br>
<textarea name="yval" id="yval" rows = "1" cols = "40" placeholder = "y-coordinate"></textarea><br>


<label for="zval"> </label><br>
<textarea name="zval" id="zval" rows = "1" placeholder = "z-coordinate"></textarea><br>

<br>
<label for="starName" style="color: white;">Or, choose target by name</label><br>
<textarea name="starName" id="starName" rows = "1" placeholder = "target star name"></textarea><br>

<br>
<label for="radius" style="color: white;"> Radius to search within</label><br>
<textarea name="radius" id="radius" rows = "1" placeholder = "radius"></textarea><br>
<br>
<input type="radio" name="whichAlgo2" value="naive1" id= "naive1"/>
<label for="naive" style="color: white;">Naive search algorithm</label>
<br>
<input type="radio" name="whichAlgo2" value="non-naive" id = "non-naive"/>
<label for="naive" style="color: white;">Advanced search algorithm</label>
<br><br>
  <input type="submit">
</form>
</p>

</#assign>

<#include "main.ftl">
