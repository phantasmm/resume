import React from 'react';
import { DropdownButton } from 'react-bootstrap';
import { MenuItem } from 'react-bootstrap';

const Time = ["0.00","1.00","2.00","3.00","4.00","5.00","6.00","7.00","8.00","9.00","10.00","11.00","12.00","13.00","14.00","15.00","16.00","17.00","18.00","19.00","20.00","21.00","22.00","23.00"];

class MyDropdown2 extends React.Component {
  constructor(props, context) {
    super(props, context);

    this.handleSelect = this.handleSelect.bind(this);

    this.state = {
      T: "Select Time"
    };
  }


  handleSelect(selected) {
    this.setState({
      T: Time[selected]
    });
  }

  render() {
    return (
        <DropdownButton
        onSelect={this.handleSelect}
        title={this.state.T}
        id="category-dropdown">
        <MenuItem eventKey="0">{Time.map(T => <div> {T} </div>)[0]}</MenuItem>
        <MenuItem eventKey="1">{Time.map(T => <div> {T} </div>)[1]}</MenuItem>
        <MenuItem eventKey="2">{Time.map(T => <div> {T} </div>)[2]}</MenuItem>
        <MenuItem eventKey="3">{Time.map(T => <div> {T} </div>)[3]}</MenuItem>
        <MenuItem eventKey="4">{Time.map(T => <div> {T} </div>)[4]}</MenuItem>
        <MenuItem eventKey="5">{Time.map(T => <div> {T} </div>)[5]}</MenuItem>
        <MenuItem eventKey="6">{Time.map(T => <div> {T} </div>)[6]}</MenuItem>
        <MenuItem eventKey="7">{Time.map(T => <div> {T} </div>)[7]}</MenuItem>
        <MenuItem eventKey="8">{Time.map(T => <div> {T} </div>)[8]}</MenuItem>
        <MenuItem eventKey="9">{Time.map(T => <div> {T} </div>)[9]}</MenuItem>
        <MenuItem eventKey="10">{Time.map(T => <div> {T} </div>)[10]}</MenuItem>
        <MenuItem eventKey="11">{Time.map(T => <div> {T} </div>)[11]}</MenuItem>
        <MenuItem eventKey="12">{Time.map(T => <div> {T} </div>)[12]}</MenuItem>
        <MenuItem eventKey="13">{Time.map(T => <div> {T} </div>)[13]}</MenuItem>
        <MenuItem eventKey="14">{Time.map(T => <div> {T} </div>)[14]}</MenuItem>
        <MenuItem eventKey="15">{Time.map(T => <div> {T} </div>)[15]}</MenuItem>
        <MenuItem eventKey="16">{Time.map(T => <div> {T} </div>)[16]}</MenuItem>
        <MenuItem eventKey="17">{Time.map(T => <div> {T} </div>)[17]}</MenuItem>
        <MenuItem eventKey="18">{Time.map(T => <div> {T} </div>)[18]}</MenuItem>
        <MenuItem eventKey="19">{Time.map(T => <div> {T} </div>)[19]}</MenuItem>
        <MenuItem eventKey="20">{Time.map(T => <div> {T} </div>)[20]}</MenuItem>
        <MenuItem eventKey="21">{Time.map(T => <div> {T} </div>)[21]}</MenuItem>
        <MenuItem eventKey="22">{Time.map(T => <div> {T} </div>)[22]}</MenuItem>
        <MenuItem eventKey="23">{Time.map(T => <div> {T} </div>)[23]}</MenuItem>
        
      </DropdownButton>
    );
  }

  
}
export default MyDropdown2