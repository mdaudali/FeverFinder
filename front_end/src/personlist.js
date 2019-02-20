import React from 'react';
import classNames from 'classnames';
import Typography from '@material-ui/core/Typography';
import ExpansionPanel from '@material-ui/core/ExpansionPanel'
import ExpansionPanelSummary from "@material-ui/core/es/ExpansionPanelSummary/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore"
import {CardContent, ExpansionPanelDetails, withStyles} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import Grid from "@material-ui/core/Grid";
import PersonListItem from './personlistitem.js';
import { styles } from './styles/searchPanelStyles.js';

class PersonList extends React.Component {

    listItemFromPerson(person) {
      return (
          <PersonListItem person={person} key={person.id} />
      );
    }

    render() {
      const { classes } = this.props;
      console.log('wuut');
      return (
          <div className={classes.elements}>
              {this.props.items.map(this.listItemFromPerson)}
          </div>
      );
    }
}

export default withStyles(styles)(PersonList);
