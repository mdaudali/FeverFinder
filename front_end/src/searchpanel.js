import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import Checkbox from '@material-ui/core/Checkbox';
import { styles } from './styles/searchPanelStyles.js';
import { examplePeople } from './exampleData.js';

function dummySearchQuery(name, callback) {
  let results = examplePeople.filter((person, ind) => Math.random() > 0.5)
  setTimeout(() => callback(results), 500);
}

class SearchPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      searchText: "",
      results: [],
    }
    this.updateList(examplePeople);
  }

  updateList(people) {
    this.setState({
      results: people.map(p => ({
        isSelected: this.props.selection.some(selected => selected.id === p.id),
        person: p,
      }))
    });
  }

  search(name) {
    dummySearchQuery(name, this.updateList.bind(this));
  }

  listItemFromPerson(entry) {
    return (
      <ListItem>
        <Checkbox
          checked={entry.isSelected}
          tabIndex={-1}
        />
        <ListItemText
          primary={entry.person.name}
          secondary={entry.person.moreInfo}
        />
      </ListItem>
    );
  }

  generateList() {
    let items = this.state.results.map(this.listItemFromPerson);
    return (
      <List>
        {[].concat(...items.map(e => [<Divider />, e])).slice(1)}
      </List>
    );
  }

  render() {

    const { classes } = this.props;

    return (
      <div>
        <Paper className={classNames(classes.searchBarRoot, classes.elements)} elevation={1}>
          <InputBase className={classes.searchBarInput} placeholder="Search patient name" />
          <IconButton aria-label="Search">
            <SearchIcon onClick={this.search.bind(this)} />
          </IconButton>
        </Paper>
        <Paper className={classes.elements}>
          {this.generateList()}
        </Paper>
      </div>
    );
  }
}

SearchPanel.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SearchPanel);
