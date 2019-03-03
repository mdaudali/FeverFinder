import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import SearchInput from './searchinput.js';
import PersonList from './personlist.js';
import { styles } from './styles/searchPanelStyles.js';

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
