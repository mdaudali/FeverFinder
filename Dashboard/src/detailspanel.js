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
    console.log(this.props.data);
    return (
            this.props.data.map((obj) => {
            if (!obj.hasOwnProperty("orderedKeys")) {
                let orderedKeys = [];
                Object.keys(obj).map(key => orderedKeys.push(key));
                obj.orderedKeys = orderedKeys;
            }
            return <PersonPanel person={obj}/>;

            })
    )
  }
}

export default DetailsPanel;

