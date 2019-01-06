var mysql = require('mysql');
var http = require('http');
const express = require('express');
const app = express() ;
const port = process.env.PORT || 5000 ;
const bodyParser = require('body-parser');
//var isExist = false ;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

var connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '*****',
  database : 'travelvanka',
    port : '3306'
  
});


connection.connect();

//app.post('/api/');

app.post('/dplop/add_dept', (req, res) => {
  let sql = 'SELECT * FROM department WHERE deptName = ? ';
  connection.query(sql , req.body['deptName'] , (error,result)=>{
   
    if(error) throw error;
    if(result.length !== 0) {
      return  ; // data is already exist
    }
    sql = 'INSERT INTO department VALUE(?,?) ';
    connection.query(sql,[req.body['deptName'],req.body['telno']], function (error, result) {
      if (error) throw error;
     // res.send('department Added');
      res.sendStatus(200);
    });
  });
});
app.get('/dplop/list_dept',(req,res)=>{
  let sql = 'SELECT * FROM department';
  
  connection.query(sql,(error , result )=>{
      if(error) throw error ;
      let list_result = [];

      let all = JSON.parse(JSON.stringify(result));
      res.send(all);
      console.log(all);
      console.log(result);
     
  });
});

app.post('/dplop/change_dept_tel',(req ,res)=>{
  let sql = 'SELECT * FROM department WHERE deptName = ?  ' ;
  connection.query(sql , [req.body['deptName']],(error , result)=>{
      if(error) throw error ;
      if(result.length == 0) {
        console.log('Department does not exist!!')
        res.sendStatus(400);
        return ;
      }
      sql = 'UPDATE department SET telno=? WHERE deptName = ?';
      connection.query(sql , [req.body['telno'],req.body['deptName']],(error , result)=>{
        if(error) throw error ;
        console.log('Change_dept_tel complete!!');
        res.sendStatus(200);
    });
  }); 
});
app.post('/dplop/remove_dept',(req,res)=>{
  let sql = 'SELECT * FROM department WHERE deptName = ?'
  connection.query(sql , [req.body['deptName']],(error,result)=>{
    if(error) throw error ;
    if(result.length==0 ){
      console.log('Department does not exist!!');
      res.sendStatus(400);
      return ;
    }
    sql = 'DELETE FROM department WHERE deptName = ?';
    connection.query(sql , [req.body['deptName']], (error , result)=>{
      if(error) throw error ;
      res.sendStatus(200);
    });
  });
});
app.post('/dplop/insert_reservation', (req,res)=>{

  //insert null or 0 into increment fields
   let reservID=null;
   let totalFare=30;
  let dateTime = new Date()
let mm = dateTime.getMonth() + 1;
let dd = dateTime.getDate();
let yy = dateTime.getFullYear();
let hr = dateTime.getHours();
let min = dateTime.getMinutes();
let sec = dateTime.getSeconds();
let myDateString = yy + '-' + mm + '-' + dd + " " + hr + ":" + min + ":" + sec;
   // insert into reservation value(reservationCode,dateTime,Totalfare,tripNo.,CustomerID)
   let sql = 'INSERT INTO reservation value(?,?,?,?,?)';
    connection.query(sql , [reservID,myDateString,totalFare,[req.body.tripNo],[req.body.customerID]],(error , result)=>{
      if(error) throw error ;
      console.log(result);
      res.sendStatus(200);
    });
});

app.post('/dplop/list_reservation',(req,res)=>{
    let sql = 'SELECT * FROM reservation WHERE customerID = ?';

    connection.query(sql , [req.body.customerID],(error , result)=>{
      if(error) throw error;
      let all = JSON.parse(JSON.stringify(result));
      res.send(all);
      // res.statusCode(200) ;
    });
});
app.post('/dplop/cancel_reservation',(req,res)=>{
    let sql = 'DELETE FROM reservation WHERE reservationCode = ?';
    connection.query(sql , [req.body.reservationCode],(error,result)=>{
      if(error) throw error;
      // res.statusCode(200);
    });
});
app.post('/dplop/update_trip',(req,res)=>{
    let sql = 'SELECT * FROM trip WHERE tripNo=?';
      connection.query(sql,[req.body.tripNo],(error,result)=>{
        if(result.length === 0) {
          console.log('TripNo not found');
          // res.statusCode(400);
          return ;
        }
        let sql2 = 'UPDATE trip SET deptTime = ? , arrvTime = ? WHERE tripNo=?';
        connection.query(sql2 , [req.body['deptTime'],req.body['arrvTime'],req.body.tripNo],(error,result)=>{
          console.log('Change trip time complete')
          // res.statusCode(200);
         
        });
      });
    
});
// app.post('/dplop/list_trip',(res,req)=>{
//     let sql = 'SELECT * FROM trip WHERE '
// });

app.post('/dplop/list_trip',(req,res)=>{
  let sql = 'select t.tripNo,s1.stationName as DeptStation, s2.stationName as ArrvStation, t.depttime,t.arrvTime,t.availSeat from   trip t, route r, station s1, station s2 WHERE  t.routeID=r.routeID and r.deptStationID=s1.stationID and r.arrvStationID=s2.stationID';
  connection.query(sql ,[req.body.routeID,req.body.deptTime,req.body.arrvTime],(error,result)=>{
    if(error) throw error ;
    let all = JSON.parse(JSON.stringify(result));
    res.statusCode(200) ;
    res.send(all);
  });
});

app.get('/addDept', (req, res) => {
  let post = {deptName:'xxxxxx', telno:'0808080808'};
  
  let sql = 'SELECT * FROM department WHERE deptName = ?';
  
  connection.query(sql , ['HumanResource'], (error,results,fields)=>{
    if(error) throw error;
    let isExist = false ;
    //console.log(results);
    console.log(results.length);
    if(results.length !== 0 ){
      console.log('inside');
      isExist = true ; // data is already exist
    }
    
    if(isExist){
      res.send('Department is Already exist'); 
      return ;
    }
    sql = 'INSERT INTO department SET ?';
    connection.query(sql ,post , (err , result)=>{
      if(err) throw err ;
      console.log(result);
      res.send('Department Added...');
    });
  });
});
app.listen(port,()=>console.log(`Listening on port ${port}`));