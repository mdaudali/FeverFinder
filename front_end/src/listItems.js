import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import MapIcon from '@material-ui/icons/Map';
import PeopleIcon from '@material-ui/icons/People';

export function NavListItems(props) {
  return (
    <div>
      <ListItem button onClick = {() => props.clickHandler("map")}>
        <ListItemIcon>
          <MapIcon />
        </ListItemIcon>
        <ListItemText primary="Map" />
      </ListItem>
      <ListItem button onClick = {() => props.clickHandler("search")}>
        <ListItemIcon>
          <PeopleIcon />
        </ListItemIcon>
        <ListItemText primary="Search patients" />
      </ListItem>
    </div>
  );
}
