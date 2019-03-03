import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import SearchBar from './searchbar.js';
import { styles } from './styles/searchPanelStyles.js';

class SearchInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            eatsWith: "",
            livesWith: "",
            worksWith: "",
            symptomScore: "",
            riskScore: "",
            symptomFormatError: false,
            riskFormatError: false,
        }
    }

    checkNumberFormat(str) {
        let n = Number(str);
        return (!isNaN(n) && n >= 0.0 && n <= 1.0);
    }

    search(name) {
        // Validate input
        let symptomError = !this.checkNumberFormat(this.state.symptomScore);
        let riskError = !this.checkNumberFormat(this.state.riskScore);
        this.setState({
            symptomFormatError: symptomError,
            riskFormatError: riskError,
        });
        if (symptomError || riskError)
            return;

        // Assemble query
        var query = '/api/people/search/?';
        if (this.state.name)
            query += 'name='+encodeURIComponent(this.state.name)+'&';
        if (this.state.eatsWith)
            query += 'eatsWith='+encodeURIComponent(this.state.eatsWith)+'&';
        if (this.state.livesWith)
            query += 'livesWith='+encodeURIComponent(this.state.livesWith)+'&';
        if (this.state.worksWith)
            query += 'worksWith='+encodeURIComponent(this.state.worksWith)+'&';
        if (this.state.symptomScore)
            query += 'symptomScore='+encodeURIComponent(this.state.symptomScore)+'&';
        if (this.state.riskScore)
            query += 'riskScore='+encodeURIComponent(this.state.riskScore)+'&';
        query = query.slice(0, -1);
        console.log("Would make query:" + query);

        // Send request
        var request = new XMLHttpRequest();

        request.open('GET', query, true);
        request.onload = (callback => function() {
            if(this.response === undefined || this.response.length == 0) {
                // TODO: nothing found
                console.log("Nothing found");
            } else {
                var ps = JSON.parse(this.response);
                callback(ps);
            }
        })(this.props.updateList);
        request.send();
    }

    handleSearchTextChange(key, value) {
        var update = {};
        update[key] = value;
        this.setState(update);
    }

    render() {
        let { classes } = this.props;
        return (
            <Grid container spacing = {8} className = { classes.controlDiv }>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Patient name"
                        header="Patient name"
                        stateKey="name"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.name} />
                </Grid>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Patient shares food with..."
                        header="Eats with"
                        stateKey="eatsWith"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.eatsWith} />
                </Grid>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Patient lives with..."
                        header="Lives with"
                        stateKey="livesWith"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.livesWith} />
                </Grid>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Patient works with..."
                        header="Works with"
                        stateKey="worksWith"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.worksWith} />
                </Grid>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Enter threshold number"
                        header="Symptom score"
                        stateKey="symptomScore"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.symptomScore}
                        number
                        error={this.state.symptomFormatError} />
                </Grid>
                <Grid item xs={2} alignContent='center'>
                    <SearchBar hint="Enter threshold number"
                        header="Risk score"
                        stateKey="riskScore"
                        changeText={this.handleSearchTextChange.bind(this)}
                        onEnter={this.search.bind(this)}
                        value = {this.state.riskScore}
                        number
                        error={this.state.riskFormatError} />
                </Grid>
            </Grid>
        );
    }
}

export default withStyles(styles)(SearchInput);