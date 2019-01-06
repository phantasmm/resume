const APPID = "TrespassingChecker";
            const KEY = "8EH2uAVv0AEIoi3";
            const SECRET = "GhEv7dGxiBvh3l5wFSOIFd2fV";
    
            const ALIAS = "HTML_web"; //  ชื่อตัวเอง
            const thing1 = "TChecker"; //  ชื่อเพื่อนที่จะคุย
    
            var microgear = Microgear.create({
                key: KEY,
                secret: SECRET,
                alias: ALIAS
            });
    
            microgear.on('message', function (topic, msg) {
                var date = Date();
                document.getElementById("recent").innerHTML =date;
                console.log(msg); // for debug
                
            });
    
            microgear.on('connected', function () {
                microgear.setAlias(ALIAS);
                document.getElementById("connected_NETPIE").innerHTML = "Connected to TrespassingChecker"
            });
    
            microgear.on('present', function (event) {
                console.log(event);
            });
    
            microgear.on('absent', function (event) {
                console.log(event);
            });
    
            microgear.resettoken(function (err) {
                microgear.connect(APPID);
            });
var request = new XMLHttpRequest();
request.open('GET', 'https://api.netpie.io/feed/PassLog?apikey=LtCJboFxMtB05t0q5xHKe2KElS3rYVoO&since=4days', true);
request.onload = function () {

  // Begin accessing JSON data here
  var data = JSON.parse(this.response);
  console.log(request.status);
  if (request.status >= 200 && request.status < 400) {
    myjson = data;
    console.log(data)
    var summary = document.getElementById("summaryTable");
    summary.innerHTML = genSummaryTable();
    var log = document.getElementById("logTable");
    log.innerHTML = json2table();
  } else {
    var error = document.getElementById("error");
    error.innerHTML = 'ERROR';
  }
}
 
var myjson = {};
var temp = 5;

function tstoTime(timestamp)
{
    var d = new Date(timestamp);
	var formattedDate = d.getDate() + "-" + (d.getMonth() + 1) + "-" + d.getFullYear();
	var hours = (d.getHours() < 10) ? "0" + d.getHours() : d.getHours();  //If it is 1 digit, fill 0 in its front. ex. 9 -> 09.
	var minutes = (d.getMinutes() < 10) ? "0" + d.getMinutes() : d.getMinutes();
	var seconds = (d.getSeconds() < 10) ? "0" + d.getSeconds() : d.getSeconds();
	var formattedTime = hours + ":" + minutes + ":" + seconds;

	formattedDate = formattedDate + " " + formattedTime;
    return formattedDate;
}

function genSummaryTable()
{
    var headerRow = '';
    var headers = '';
    var valueRows = '';
    var valueRow = '';
    var values = '';
    var table = '';
    headers += '<th>Time</th><th>Total Passes</th>';
    headerRow = '<tr>' + headers + '</tr>';
    var dEnd = new Date(Date());
    //document_write(d_end);
    var dStart = new Date(Date());
    dStart.setMinutes(dStart.getMinutes() - 15); 
    var count;
    var i = myjson.data[0].values.length-1;
    while(1)
    {
       if(i<0) break;
       count = 0;
       while(1)
       {
          if(i<0) break;
          if(myjson.data[0].values[i][0] < dStart) break;
          count++;
          i--;
       }
       if(count != 0)
       {
       values = '';
       values+= '<td><center>' + tstoTime(dStart) + " - " + tstoTime(dEnd) + '</center></td>';
       values+= '<td><center>' + count + '</center></td>';
       valueRow = '<tr>'+ values + '</tr>';  //one value row
       valueRows = valueRows + valueRow; //all value rows
       }
       dStart.setMinutes(dStart.getMinutes() - 15); 
       dEnd.setMinutes(dEnd.getMinutes() - 15);
    } 
    table = '<table>' + headerRow + valueRows + '</table>';
    return table;
}
function checkTime(timestamp)
{
    var d = new Date(timestamp); 
    if(d.getDate() == 17 && d.getMonth()+1==12 && d.getFullYear()==2018)
        return true;
    return false;
}

function json2table() //generating html code of json data 
{
    var headerRow = '';
    var headers = '';
    var valueRows = '';
    var valueRow = '';
    var values = '';
    var table = '';
    var count_people = 0;
    headers += '<th>Time</th>';
    for(i=0; i < myjson.data.length; i++)
    {
    	headers += '<th>' + "passing time " + "(" + "seconds" + ")" + '</th>';
    }
    headerRow = '<tr>' + headers + '</tr>';
    for(i=0; i < myjson.data[0].values.length; i++)
    {
        values = '';
        if(checkTime(myjson.data[0].values[i][0])){
        count_people++;
        values+= '<td><center>' + tstoTime(myjson.data[0].values[i][0]) + '</center></td>';  //timestamp
        for(j=0; j < myjson.data.length; j++)
        {
            values+= '<td><center>' + myjson.data[j].values[i][1] + '</center></td>';  //values entry
        }
        valueRow = '<tr>'+ values + '</tr>';  //one value row
        valueRows = valueRows + valueRow; //all value rows
        }
    }
    table = '<table>' + headerRow + valueRows + '</table>';
    return table;
}

request.send();    


