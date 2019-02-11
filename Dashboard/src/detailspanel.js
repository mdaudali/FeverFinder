import React from 'react';
import classNames from 'classnames';
import Typography from '@material-ui/core/Typography';
import ExpansionPanel from '@material-ui/core/ExpansionPanel'
import ExpansionPanelSummary from "@material-ui/core/es/ExpansionPanelSummary/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore"
import {CardContent, ExpansionPanelDetails, withStyles} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import PersonPanel from "./personpanel";



class DetailsPanel extends React.Component {
  render() {
    const personObj = {
          name: "Mohammed Daudali",
          age: 18,
          other: {
            "Hi": "Mohammed",
            orderedValues: ["Hi", "hello"]
          },
          orderedValues: ["name", "age", "other"]
        };
    return (
        <PersonPanel person={personObj}/>
  )
  }
}

export default DetailsPanel;

