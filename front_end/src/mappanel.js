import React from 'react';
import { compose, withProps, lifecycle } from "recompose"
import {
  activeMarker,
  withHandlers,
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  Marker,
  InfoWindow
} from "react-google-maps"

import HeatmapLayer from "react-google-maps/lib/components/visualization/HeatmapLayer";

const _ = require("lodash");
const { SearchBox } = require("react-google-maps/lib/components/places/SearchBox");



class MapPanel extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      mapData: [],
      features: [],
      selectedMarker: false
    }
  }

  componentDidMount() {
      fetch("http://13.95.172.26:8000/api/people/?format=json")
      .then(r => r.json())
      .then(data => {
        {Object.keys(data).map((key) =>
          (
            this.setState({
              features: data,
              mapData: this.state.mapData.concat(
                data[key].store_gps_latitude,
                data[key].store_gps_longitude,
                data[key].sick
              )
            })
          )
        )}
      })
  }

  handleClick = (marker) => {
    this.setState({ selectedMarker: marker })
    this.forceUpdate();
  }
  render() {
    return (
      <div>
        <MapWithASearchBox
          selectedMarker={this.state.selectedMarker}
          markers={this.state.features}
          onClick={this.handleClick}
          heatmapRawData={this.state.mapData}
        />
      </div>

    )
  }
}

const MapWithASearchBox = compose(
  withProps({
    googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyByrMnwOjTCjDZFgw_P2VXJJo6qxOVseC0&v=3.exp&libraries=geometry,drawing,places,visualization",
    loadingElement: <div style={{ height: `100%` }} />,
    containerElement: <div style={{ height: `850px` }} />,
    mapElement: <div style={{ height: `100%` }} />,
  }),

  lifecycle({

    componentWillMount() {
      const refs = {}

      this.setState({
        bounds: null,
        center: {
          lat: 6.4, lng: 8.0
        },
        isHidden: true,
        onToggle: () => {
          this.setState({
            isHidden: !this.state.isHidden
          })
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
          });
        },
      })
    },

    componentWillReceiveProps(nextProps) {
      var filteredData = [];
      if (nextProps.heatmapRawData !== this.state.data) {
        this.setState({
          data: nextProps.heatmapRawData
        });
        for (var i = 0; i < nextProps.heatmapRawData.length - 3; i+=3) {
          const htmapPoint = {
            location: new window.google.maps.LatLng(
              nextProps.heatmapRawData[i],
              nextProps.heatmapRawData[i+1],
            ),
            weight:  nextProps.heatmapRawData[i+2],
          };
          filteredData.push(htmapPoint);
        }
        // update heatmap data
        this.setState({mapData: filteredData});
      }
    },

  }),
  withScriptjs,
  withGoogleMap
)(props => {
    return (
  <GoogleMap
    ref={props.onMapMounted}
    defaultZoom={10}
    center={props.center}
    onBoundsChanged={props.onBoundsChanged}
    defaultCenter={{ lat: 6.4, lng: 8.0}}
  >

    <HeatmapLayer
      data={props.mapData}
      options={{radius: 20}}>
    </HeatmapLayer>

    {props.markers.map(marker => {
      const onClick = props.onClick.bind(this, marker);
      return (
        <Marker
          key={marker.id}
          visible={!props.isHidden}
          onClick={onClick}
          position={{ lat: marker.store_gps_latitude, lng: marker.store_gps_longitude }}
        >
          {props.selectedMarker === marker && !props.isHidden &&
            <InfoWindow>
              <div>
                {marker.patient_name}
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

      <div>
        <div
          style={{float: `left`}}
        >
          <input
            type="text"
            placeholder="Search..."
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
        </div>

        <div
          style={{
            float: `right`,
            padding: `0 20px`
          }}
        >
          <button
            onClick={props.onToggle}
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
          > Display/Hide Markers </button>
        </div>

      </div>

    </SearchBox>

    {props.markers.map((marker, index) =>
      <Marker
        key={index}
        position={marker.position} />
    )}

  </GoogleMap>
    )}
);

export default MapPanel;
