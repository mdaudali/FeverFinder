import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import { styles } from './styles/searchPanelStyles.js';

class SearchBar extends React.Component {


    render() {
        const { classes } = this.props;

        return (
          <Paper className={classNames(classes.searchBarRoot, classes.elements)} elevation={1}>
            <InputBase className={classes.searchBarInput}
              placeholder={this.props.hintText}
              onChange={this.props.onChange}
              value={this.props.value}
              onKeyPress={e => {if (e.key === 'Enter') this.props.onEnter()}} />
          </Paper>
        );
    }
}

export default withStyles(styles)(SearchBar);
