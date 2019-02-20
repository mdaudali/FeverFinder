import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import SearchBar from './searchbar.js';
import { styles } from './styles/searchPanelStyles.js';
import { examplePeople } from './fullpeopleexample.js';

function dummySearchQuery(name, callback) {
  let results = examplePeople.filter((person, ind) => Math.random() > 0.5)
  setTimeout(() => callback(results), 100);
}

function uuidSearchQuery(name, callback) {
  var request = new XMLHttpRequest();
  request.open('GET', 'http://127.0.0.1:8000/api/people/get_by_id/?id=7d516f50-02bf-40c3-8e83-5bc4321b8861', true);
  request.onload = function() {
    var ps = JSON.parse(this.response);
    ps[0].name = "Some Name";
    callback(ps);
  }
  request.send();
}

class SearchInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchText: "",
        }
    }

    search(name) {
      console.log("wut");
      dummySearchQuery(name, this.props.updateList);
    }

    handleSearchTextChange(event) {
      this.setState({
        searchText: event.target.value,
      });
    }

    render() {
        return (
            <SearchBar hintText="Enter patient name" onChange={this.handleSearchTextChange.bind(this)} onEnter={this.search.bind(this)} value = {this.state.searchText} />
        );
    }
}

export default withStyles(styles)(SearchInput);
