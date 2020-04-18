import React from "react";

// core components
import { Row, Col } from "reactstrap";
import PropagateLoader from "react-spinners/PropagateLoader";
import LineChart from "components/Weather/LineChart.js";
import WeatherTable from "components/Weather/WeatherTable.js";

// firestore
import firebase from "firebase/firestore.js";

class Weather extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      ids: [],
      weatherData: [],
      feelsLikeData: [],
      generalConditionData: [],
      humidityData: [],
      windData: [],
    };

    this.getWeatherData();
  }

  getWeatherData() {
    const db = firebase.firestore();

    db.collection("WM")
      .get()
      .then((querySnapshot) => {
        let docs = querySnapshot.docs.slice(-24);
        let docs_data = docs.map((doc) => doc.data());

        this.setState({
          ids: docs.map((doc) => doc.id),
          weatherData: docs_data.map((data) =>
            (data["temp"] - 273.15).toFixed(0)
          ),
          feelsLikeData: docs_data.map((data) =>
            (data["feels_like"] - 273.15).toFixed(0)
          ),
          icons: docs_data.map((data) => data["icon"]),
          humidityData: docs_data.map((data) => data["humidity"]),
          windSpeedData: docs_data.map((data) => data["wind_speed"]),
          windDegData: docs_data.map((data) => data["wind_deg"]),
        });
      });
  }

  render() {
    if (
      this.state.ids.length === 0 ||
      this.state.feelsLikeData.length === 0 ||
      this.state.weatherData.length === 0
    ) {
      return (
        <div className="content">
          <Row>
            <Col
              xs="12"
              className="text-center"
              style={{ paddingTop: "30%", paddingLeft: "50%" }}
            >
              <PropagateLoader color={"#1E8AF7"} />
            </Col>
          </Row>
        </div>
      );
    }
    return (
      <div className="content">
        <LineChart
          title="Weather"
          subtitle="Braga, Portugal"
          labels={this.state.ids}
          weatherData={this.state.weatherData}
          feelsLikeData={this.state.feelsLikeData}
        />
        <Row>
          <WeatherTable
            labels={this.state.ids}
            icons={this.state.icons}
            humidityData={this.state.humidityData}
            windSpeedData={this.state.windSpeedData}
            windDegData={this.state.windDegData}
          />
        </Row>
      </div>
    );
  }
}

export default Weather;
