import React, { Component } from 'react';
import ControlledCarousel from './ControlledCarousel';

import 'react-day-picker/lib/style.css'
import logo from './logo.svg';
import './App.css';

import MyDropdown1 from './MyDropdown1'
import MyDropdown2 from './MyDropdown2'
import MyDatePicker from'./MyDatePicker'


class App extends Component {

  constructor(props, context) {
    super(props, context);
    this.state = {
      

    }
  }

  render() {
    return (
      <React.Fragment>
        <div className="App">
        <center>
          <h1>TrespassingChecker</h1>
          <div id="connected_NETPIE"></div>
          <p class="outset">Recent Pass: <div id="recent" class="red"></div></p>
          <MyDropdown1></MyDropdown1>
           <table className="select-date">
            <tr>
              <th>Start Time</th>
              <th>End Time</th>
            </tr>
            <tr>
            <td>
              <MyDropdown2></MyDropdown2>
            </td>
            <td>
              <MyDropdown2></MyDropdown2>
            </td>

            </tr>
            <tr>
            <td>
              <MyDatePicker></MyDatePicker>
            </td>
            <td>
              <MyDatePicker></MyDatePicker>
            </td>
            
            </tr>

          </table>
          <div><h4>Passing Summary</h4></div>
          <table id="summaryTable"></table>
          <div><h4>Passing log</h4></div>
          <table id="logTable"></table>
          <p id="error"></p>
        </center>
          
          <header className="App-containers">

         

          </header>
        </div>
      </React.Fragment>
    );
  }
}



export default App;
