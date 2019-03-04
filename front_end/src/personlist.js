import React from 'react';
import {withStyles} from "@material-ui/core";
import PersonListItem from './personlistitem.js';
import {styles} from './styles/searchPanelStyles.js';

class PersonList extends React.Component {

    listItemFromPerson(person) {
      return (
          <PersonListItem person={person} key={person.id} />
      );
    }

    render() {
      const { classes } = this.props;
      return (
          <div>
              {this.props.items.map(this.listItemFromPerson)}
          </div>
      );
    }
}

export default withStyles(styles)(PersonList);
