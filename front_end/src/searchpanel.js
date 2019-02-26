import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import SearchInput from './searchinput.js';
import InputBase from '@material-ui/core/InputBase';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import PersonList from './personlist.js';
import { styles } from './styles/searchPanelStyles.js';
import { examplePeople } from './fullpeopleexample.js';

class SearchPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      searchText: "",
      results: [],
    }
  }

  updateList(people) {
    this.setState({
      results: people
    });
  }

  render() {

    const { classes } = this.props;

    return (
      <div className={classes.elements}>
        <SearchInput updateList={this.updateList.bind(this)} />
        <PersonList items={this.state.results}/>
      </div>
    );
  }
}

SearchPanel.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SearchPanel);
