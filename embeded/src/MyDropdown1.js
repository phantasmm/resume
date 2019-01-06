import React from 'react';
import { DropdownButton } from 'react-bootstrap';
import { MenuItem } from 'react-bootstrap';

const Time = ["15seconds", "1minute", "15minutes", "30minutes", "1hour","6hours","1days"];

class MyDropdown1 extends React.Component {
  constructor(props, context) {
    super(props, context);

    this.handleSelect = this.handleSelect.bind(this);

    this.state = {
      T: "Block Size"
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
        <MenuItem divider />
        <MenuItem eventKey="1">{Time.map(T => <div> {T} </div>)[1]}</MenuItem>
        <MenuItem eventKey="2">{Time.map(T => <div> {T} </div>)[2]}</MenuItem>
        <MenuItem eventKey="3">{Time.map(T => <div> {T} </div>)[3]}</MenuItem>
        <MenuItem divider />
        <MenuItem eventKey="4">{Time.map(T => <div> {T} </div>)[4]}</MenuItem>
        <MenuItem eventKey="5">{Time.map(T => <div> {T} </div>)[5]}</MenuItem>
        <MenuItem divider />
        <MenuItem eventKey="6">{Time.map(T => <div> {T} </div>)[6]}</MenuItem>
      </DropdownButton>
    );
  }

  
}



export default MyDropdown1