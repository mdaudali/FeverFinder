import React from 'react';
import { compose, withProps, lifecycle } from "recompose"
import classNames from 'classnames';
import {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  Marker,
  InfoWindow
} from "react-google-maps"
import Typography from '@material-ui/core/Typography';

const _ = require("lodash");
const { SearchBox } = require("react-google-maps/lib/components/places/SearchBox");

class MapPanel extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      features: [],
      selectedMarker: false
    }
  }
  componentDidMount() {
    console.log("inside handleGetJson");
    // the link is just a JSON a hosting and storage service
    // which contains the same data as the people JSON
    fetch("https://api.myjson.com/bins/1bnvni")
      .then(r => r.json())
      .then(data => {
          console.log(data);
        this.setState({ features: data.features })
      })
  }
  handleClick = (marker, event) => {
    console.log({ marker })
    this.setState({ selectedMarker: marker })
  }
  render() {
    return (

      <div>
        {/*<MapWithAMarker*/}
          {/*selectedMarker={this.state.selectedMarker}*/}
          {/*markers={this.state.features}*/}
          {/*onClick={this.handleClick}*/}
        {/*/>*/}
        <MapWithASearchBox selectedMarker={this.state.selectedMarker} markers={this.state.features} onClick={this.handleClick}/>

      </div>

    )
  }
}
const MapWithAMarker = compose(
  withProps({
    googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyByrMnwOjTCjDZFgw_P2VXJJo6qxOVseC0&v=3.exp&libraries=geometry,drawing,places",
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `400px` }} />,
    mapElement: <div style={{ height: `100%` }} />,
  }),
  withScriptjs,
  withGoogleMap)(props => {
    return (
      <GoogleMap
        ref={props.onMapMounted}
        defaultZoom={8}

        onBoundsChanged={props.onBoundsChanged}

        defaultCenter={{ lat: 0.8541553715898037, lng: 32.640380859375 }}
      >
        {props.markers.map(marker => {
          const onClick = props.onClick.bind(this, marker)
          return (
            <Marker
              key={marker.id}
              onClick={onClick}
              position={{ lat: marker.latitude, lng: marker.longitude }}
            >
              {props.selectedMarker === marker &&
                <InfoWindow>
                  <div>
                    {marker.name}
                  </div>
                </InfoWindow>
              }
            </Marker>
          )
        })}
      </GoogleMap>
  )
});


const MapWithASearchBox = compose(
  withProps({
    googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyByrMnwOjTCjDZFgw_P2VXJJo6qxOVseC0&v=3.exp&libraries=geometry,drawing,places",
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `400px` }} />,
    mapElement: <div style={{ height: `100%` }} />,
  }),
  lifecycle({
    componentWillMount() {
      const refs = {}

      this.setState({
        bounds: null,
        center: {
          lat: 52.210945, lng:  0.091719
        },
        onMapMounted: ref => {
          refs.map = ref;
        },
        onBoundsChanged: () => {
          this.setState({
            bounds: refs.map.getBounds(),
            center: refs.map.getCenter(),
          })
        },
        onSearchBoxMounted: ref => {
          refs.searchBox = ref;
        },
        onPlacesChanged: () => {
          const places = refs.searchBox.getPlaces();
          const bounds = new window.google.maps.LatLngBounds();

          places.forEach(place => {
            if (place.geometry.viewport) {
              bounds.union(place.geometry.viewport)
            } else {
              bounds.extend(place.geometry.location)
            }
          });
          const nextMarkers = places.map(place => ({
            position: place.geometry.location,
          }));
          const nextCenter = _.get(nextMarkers, '0.position', this.state.center);

          this.setState({
            center: nextCenter,
            markers: nextMarkers,
          });
        },
      })
    },
  }),
  withScriptjs,
  withGoogleMap
)(props => {
    return (
  <GoogleMap
    ref={props.onMapMounted}
    defaultZoom={8}
    center={props.center}
    onBoundsChanged={props.onBoundsChanged}
    defaultCenter={{ lat: 0.8541553715898037, lng: 32.640380859375 }}

  >
      {props.markers.map(marker => {
          const onClick = props.onClick.bind(this, marker);
          return (
            <Marker
              key={marker.id}
              onClick={onClick}
              position={{ lat: marker.latitude, lng: marker.longitude }}
            >
              {props.selectedMarker === marker &&
                <InfoWindow>
                  <div>
                    {marker.name}
                  </div>
                </InfoWindow>
              }
            </Marker>
          )
        })}
    <SearchBox
      ref={props.onSearchBoxMounted}
      bounds={props.bounds}
      controlPosition={window.google.maps.ControlPosition.TOP_LEFT}
      onPlacesChanged={props.onPlacesChanged}
    >
      <input
        type="text"
        placeholder="Customized your placeholder"
        style={{
          boxSizing: `border-box`,
          border: `1px solid transparent`,
          width: `240px`,
          height: `32px`,
          marginTop: `15px`,
          padding: `0 12px`,
          borderRadius: `3px`,
          boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
          fontSize: `14px`,
          outline: `none`,
          textOverflow: `ellipses`,
        }}
      />
    </SearchBox>
    {props.markers.map((marker, index) =>
      <Marker key={index} position={marker.position} />
    )}
  </GoogleMap>
    )}
);

export default MapPanel;
