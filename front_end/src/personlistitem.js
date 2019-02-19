import React from 'react';
import classNames from 'classnames';
import Typography from '@material-ui/core/Typography';
import ExpansionPanel from '@material-ui/core/ExpansionPanel'
import ExpansionPanelSummary from "@material-ui/core/es/ExpansionPanelSummary/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore"
import {CardContent, ExpansionPanelDetails, withStyles} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import Grid from "@material-ui/core/Grid";

// TODO: In an internal map, provide components instead of text
const styles = theme => ({
    card: {
        padding: 0
    },
  root: {
    width: '100%',
    boxShadow: "None",
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    flexBasis: '33.33%',
    flexShrink: 0,
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
    color: theme.palette.text.secondary,
  },
  expansionPanel: {
      width: '100%',
  }
});

class PersonListItem extends React.Component {
    constructor(props) {
        super(props);
        this.createComponent = this.createComponent.bind(this);
    }

    makeGridItem(v) {

    }

    expandMap(map) {
        if (!map.hasOwnProperty("orderedKeys")) {
            let orderedKeys = [];
            Object.keys(map).map(key => orderedKeys.push(key));
            map.orderedKeys = orderedKeys;
        }
        return (
          <Grid container xs={12}>
            {map.orderedKeys.map((v) => (
              <Grid item xs={12}>
                {this.createComponent(v, map[v])}
              </Grid>
            ))}
          </Grid>
        );
    }

    createComponent(key, value) {
        const { classes, person } = this.props;
        if (!(value instanceof Object)) {
            return (
                <ExpansionPanel expanded={false} className={classes.expansionPanel}>
                 <ExpansionPanelSummary>
                    <Typography className={classes.heading}>{key.charAt(0).toUpperCase() + key.substr(1)}</Typography>
                    <Typography className={classes.secondaryHeading}>{value}</Typography>
                  </ExpansionPanelSummary>
                </ExpansionPanel>
            )
        }
        else {
            return (
                <ExpansionPanel className={classes.expansionPanel}>
                 <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                    <Typography className={classes.heading}>{key.charAt(0).toUpperCase() + key.substr(1)}</Typography>
                  </ExpansionPanelSummary>
                    <ExpansionPanelDetails>
                        {this.expandMap(value)}
                    </ExpansionPanelDetails>
                </ExpansionPanel>
            )
        }

    }
  render() {
      return this.createComponent("Some Guy", this.props.person);
    }
}

export default withStyles(styles)(PersonListItem);
