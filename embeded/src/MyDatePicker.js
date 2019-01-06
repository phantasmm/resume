import React from 'react';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';

export default class MyDatePicker extends React.Component {
  constructor(props) {
    super(props);
    this.handleDayClick = this.handleDayClick.bind(this);
    this.state = {
      selectedDay: null,
    };
  }
  handleDayClick(day, { selected }) {
    this.setState({
      selectedDay: selected ? undefined : day,
    });
    if(this.state.selectedDay !== null)
      console.log(this.state.selectedDay.toLocaleDateString())
  }
  render() {
    return (
      <div>
        <p>
          {this.state.selectedDay
            ? this.state.selectedDay.toLocaleDateString()
            : 'Please select a day'}
        </p>
        <DayPicker
          selectedDays={this.state.selectedDay}
          onDayClick={this.handleDayClick}
        />
        
      </div>
    );
  }
}

