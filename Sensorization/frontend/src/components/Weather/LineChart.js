import React from "react";
import PropTypes from "prop-types"; // ES6

// reactstrap components
import {
  Button,
  ButtonGroup,
  Card,
  CardHeader,
  CardBody,
  CardTitle,
  Row,
  Col,
} from "reactstrap";

// react plugin used to create charts
import { Line } from "react-chartjs-2";

// nodejs library that concatenates classes
import classNames from "classnames";

let formatLabels = (docs_ids) => {
  let formated = [];
  docs_ids.map((id) => {
    let date = new Date(id);

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    formated.push(`${hours}:00h`); //date.getMinutes() > 30 ? hours+1 : hours;
  });

  return formated;
};

let lineChartOptions = {
  maintainAspectRatio: false,
  legend: {
    display: false,
  },
  tooltips: {
    backgroundColor: "#f5f5f5",
    titleFontColor: "#333",
    bodyFontColor: "#666",
    bodySpacing: 4,
    xPadding: 12,
    mode: "nearest",
    intersect: 0,
    position: "nearest",
  },
  responsive: true,
  scales: {
    yAxes: [
      {
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: "rgba(29,140,248,0.0)",
          zeroLineColor: "transparent",
        },
        ticks: {
          suggestedMin: 0,
          suggestedMax: 35,
          padding: 20,
          fontColor: "#9a9a9a",
        },
      },
    ],
    xAxes: [
      {
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: "rgba(29,140,248,0.1)",
          zeroLineColor: "transparent",
        },
        ticks: {
          padding: 20,
          fontColor: "#9a9a9a",
        },
      },
    ],
  },
};

let weatherChart = (canvas) => {
  let ctx = canvas.getContext("2d");
  let gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

  gradientStroke.addColorStop(1, "rgba(29,140,248,0.2)");
  gradientStroke.addColorStop(0.4, "rgba(29,140,248,0.0)");
  gradientStroke.addColorStop(0, "rgba(29,140,248,0)"); //blue colors

  
  return {
    labels: getLabels(labels, displayMode),
    datasets: [
      {
        label: "(ºC)",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: "#1f8ef1",
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: "#1f8ef1",
        pointBorderColor: "rgba(255,255,255,0)",
        pointHoverBackgroundColor: "#1f8ef1",
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: getDataToDisplay(displayDataType, displayMode),
      },
    ],
  };
};

let getLabels = (labels, displayMode) => {
  if(specificDay)
      return getSpecificLabels(labels);

  if (displayMode === "24h") return formatLabels(labels.slice(-24));

  let dateSet = new Set();

  labels.map((label) => dateSet.add((new Date(label)).toDateString()));

  return Array.from(dateSet.values()).slice(-8,-1);
};

let getDataToDisplay = (displayDataType, displayMode) => {

  if(specificDay)
    return getSpecificData(displayDataType)

  let displayData = displayDataType === "weather" ? [...weather] : [...feelsLike]

  if (displayMode === "24h") return displayData.slice(-24);

  let days =  getLabels(labels, displayMode)
  let count = new Array(0,0,0,0,0,0,0)
  let sum = new Array(0,0,0,0,0,0,0)

  for(let i in labels){
    let date = (new Date(labels[i])).toDateString()
    let index = days.indexOf(date)
    if(index !== -1){
      count[index] += 1

      if(isNaN( displayData[i]))sum[index] += 0
      else sum[index] += Number(displayData[i])
    }
  }

  let meanTempPerDay = new Array(0,0,0,0,0,0,0)
  for(let i in days){
    meanTempPerDay[i] = (sum[i]/count[i]).toFixed(0)
  }

  return meanTempPerDay;
};

let getSpecificLabels = (labels) => {
  let specificLabels = labels.filter((label) => (new Date(label)).toDateString() === specificDay)
  return formatLabels(specificLabels);
}

let getSpecificData = (displayDataType) => {

  let displayData = displayDataType === "weather" ? [...weather] : [...feelsLike]

  let data = []
  for(let i in labels){
    if((new Date(labels[i])).toDateString() == specificDay){
      data.push(displayData[i]);
    }
  }
  return data;
}

let labels = [];
let weather = [];
let feelsLike = [];
let displayDataType = "";
let displayMode = "";
let specificDay;

class LineChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      title: this.props.title,
      subtitle: this.props.subtitle,
      labels: this.props.labels,
      weather: this.props.weatherData,
      feelsLike: this.props.feelsLikeData,
      specificDay: this.props.specificDay,
      displayDataType: "weather",
      displayMode: "24h",
    };

    labels = this.state.labels;
    weather = this.state.weather;
    feelsLike = this.state.feelsLike;
    specificDay = this.state.specificDay;
    displayDataType = "weather";
    displayMode = "24h";

    console.log(specificDay)
  }

  setDisplayDataType = (name) => {
    this.setState({
      displayDataType: name,
    });
    displayDataType = name;
  };

  setDisplayMode = (mode) => {
    this.setState({
      displayMode: mode,
    });
    displayMode = mode;
  };

  render() {
    return (
      <Row>
        <Col xs="12">
          <Card className="card-chart">
            <CardHeader>
              <Row>
                <Col className="text-left" sm="4">
                  <h5 className="card-category">{this.state.subtitle}</h5>
                  <CardTitle tag="h2">{this.state.title}</CardTitle>
                </Col>
                <Col>
                  <ButtonGroup
                    className="btn-group-toggle float-right"
                    data-toggle="buttons"
                  >
                    <Button
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayDataType === "weather",
                      })}
                      color="info"
                      id="0"
                      size="sm"
                      onClick={() => this.setDisplayDataType("weather")}
                    >
                      <input
                        defaultChecked
                        className="d-none"
                        name="options"
                        type="radio"
                      />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Weather
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-single-02" />
                      </span>
                    </Button>
                    <Button
                      color="info"
                      id="1"
                      size="sm"
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayDataType === "feelsLike",
                      })}
                      onClick={() => this.setDisplayDataType("feelsLike")}
                    >
                      <input className="d-none" name="options" type="radio" />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Feels Like
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-gift-2" />
                      </span>
                    </Button>
                  </ButtonGroup>
                </Col>
                {!specificDay  && <Col sm="2">
                  <ButtonGroup
                    className="btn-group-toggle float-right"
                    data-toggle="buttons"
                  >
                    <Button
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayMode === "24h",
                      })}
                      color="info"
                      id="0"
                      size="sm"
                      onClick={() => this.setDisplayMode("24h")}
                    >
                      <input
                        defaultChecked
                        className="d-none"
                        name="options"
                        type="radio"
                      />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Last 24h
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-single-02" />
                      </span>
                    </Button>
                    <Button
                      color="info"
                      id="1"
                      size="sm"
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayMode === "week",
                      })}
                      onClick={() => this.setDisplayMode("week")}
                    >
                      <input className="d-none" name="options" type="radio" />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        Last Week
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-gift-2" />
                      </span>
                    </Button>
                  </ButtonGroup>
                </Col>}
              </Row>
            </CardHeader>
            <CardBody>
              <div className="chart-area">
                <Line data={weatherChart} options={lineChartOptions} />
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  }
}

export default LineChart;

LineChart.propTypes = {
  labels: PropTypes.array,
};
