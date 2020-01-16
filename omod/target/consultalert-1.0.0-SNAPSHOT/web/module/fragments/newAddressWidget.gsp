
<script>
jq=jQuery;
jq(function() { 
document.getElementById("showalert").style.display = "none";
if (${alertId} > 0) {
document.getElementById("showalert").style.display = "block";
jq("#purgeButton").css("background-color", "red");
}
})

function purgeAlert() {
      jq.getJSON('${ ui.actionLink("purgeAlert") }',
            {
              'patientId': ${patientId},
              'alertId' : ${alertId}
            })
        .success(function(data) {
            document.getElementById("showalert").style.display = "none";
        })
        .error(function(xhr, status, err) {
            alert('AJAX error ' + err);
        }) 
        
}
</script>

<div class="info-section">
<div id="showalert">
<button id="purgeButton" style="background-color:red" onclick="purgeAlert()">Purge Alert</button>
</div>
</div>

