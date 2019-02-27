import React from 'react';
import classNames from 'classnames';
import Typography from '@material-ui/core/Typography';
import ExpansionPanel from '@material-ui/core/ExpansionPanel'
import ExpansionPanelSummary from "@material-ui/core/es/ExpansionPanelSummary/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore"
import {CardContent, ExpansionPanelDetails, withStyles} from "@material-ui/core";
import Card from "@material-ui/core/Card";
import Grid from "@material-ui/core/Grid";

const localStyles = theme => ({
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

    expandMap(map) {
        if (!map.hasOwnProperty("orderedKeys")) {
            let orderedKeys = [];
            Object.keys(map).map(key => orderedKeys.push(key));
            map.orderedKeys = orderedKeys;
        }
        return (
          <Grid container>
            {map.orderedKeys.map((v) => (
              <Grid item xs={12} key={v}>
                {this.createSubcomponent(v, map[v])}
              </Grid>
            ))}
          </Grid>
        );
    }

    createComponent(heading, subheading, contents) {
      const { classes, person } = this.props;
      if (!(contents instanceof Object)) {
          return (
              <ExpansionPanel square expanded={false} className={classes.expansionPanel}>
               <ExpansionPanelSummary>
                  <Typography className={classes.heading}>{heading.charAt(0).toUpperCase() + heading.substr(1)}</Typography>
                  <Typography className={classes.secondaryHeading}>{contents+''}</Typography>
                </ExpansionPanelSummary>
              </ExpansionPanel>
          )
      }
      else {
          return (
              <ExpansionPanel square className={classes.expansionPanel}>
               <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                  <Typography className={classes.heading}>{heading.charAt(0).toUpperCase() + heading.substr(1)}</Typography>
                  <Typography className={classes.secondaryHeading}>{subheading}</Typography>
                </ExpansionPanelSummary>
                  <ExpansionPanelDetails>
                      {this.expandMap(contents)}
                  </ExpansionPanelDetails>
              </ExpansionPanel>
          )
      }
    }

    createSubcomponent(key, value) {
        return this.createComponent(key, "", value);
    }

    render() {
        return this.createComponent(
          this.props.person.patient_name,
          this.props.person.age + " years old " + this.props.person.gender + " from " + this.props.person.village_name,
          this.props.person);
    }
}

export default withStyles(localStyles)(PersonListItem);
