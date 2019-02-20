import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import FormControl from '@material-ui/core/FormControl';
import FormHelperText from '@material-ui/core/FormHelperText';
import { styles } from './styles/searchPanelStyles.js';

class SearchBar extends React.Component {

    handleChange(e) {
        var text = e.target.value;
        if (this.props.number)
            text = text.replace(/[^\d.-]/g, '');
        this.props.changeText(this.props.stateKey, text);
    }

    render() {
        const { classes } = this.props;

        return (
            <div>
              <Typography variant="h6" noWrap>{this.props.header}</Typography>
              <FormControl fullWidth className={classes.searchBarFormControl} error={this.props.error}>
              <Paper className={classes.searchBarRoot} elevation={1}>
                <InputBase className={classes.searchBarInput}
                  placeholder={this.props.hint}
                  onChange={this.handleChange.bind(this)}
                  value={this.props.value}
                  onKeyPress={e => {if (e.key === 'Enter') this.props.onEnter()}} />
              </Paper>
              <FormHelperText>{this.props.error ? "Must be number between 0 and 1" : ""}</FormHelperText>
              </FormControl>
            </div>
        );
    }
}

export default withStyles(styles)(SearchBar);
