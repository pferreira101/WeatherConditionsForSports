import React from "react";

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
  return docs_ids.map((id) => {
    let date = new Date(id);

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    return `${hours}:00h`; //date.getMinutes() > 30 ? hours+1 : hours;
  });
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
          suggestedMin: 5,
          suggestedMax: 6,
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

let uvChart = (canvas) => {
  let ctx = canvas.getContext("2d");
  let gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

  gradientStroke.addColorStop(1, "rgba(29,140,248,0.2)");
  gradientStroke.addColorStop(0.4, "rgba(29,140,248,0.0)");
  gradientStroke.addColorStop(0, "rgba(29,140,248,0)"); //blue colors

  return {
    labels: displayLabel === "uv" ? [...labelsUv] : [...labelsUvMax],
    datasets: [
      {
        label: "(UV)",
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
        data: displayData === "uv" ? [...uv] : [...uvMax],
      },
    ],
  };
};

let labelsUv = [];
let labelsUvMax = [];
let uv = [];
let uvMax = [];
let displayData = "";
let displayLabel = "";

class LineChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      title: this.props.title,
      subtitle: this.props.subtitle,
      labelsUv: formatLabels(this.props.idsUv),
      labelsUvMax: this.props.idsUvMax,
      uv: this.props.uvData,
      uvMax: this.props.uvMaxData,
      displayLabel: "uv",
      displayData: "uv",
    };
    labelsUv = this.state.labelsUv;
    labelsUvMax = this.state.labelsUvMax;
    uv = this.state.uv;
    uvMax = this.state.uvMax;
    displayLabel = "uv";
    displayData = "uv";
  }

  setDisplayData = (name) => {
    this.setState({
      displayData: name,
      displayLabel: name,
    });
    displayData = name;
    displayLabel = name;
  };

  render() {
    return (
      <Row>
        <Col xs="12">
          <Card className="card-chart">
            <CardHeader>
              <Row>
                <Col className="text-left" sm="6">
                  <h5 className="card-category">{this.state.subtitle}</h5>
                  <CardTitle tag="h2">{this.state.title}</CardTitle>
                </Col>
                <Col sm="6">
                  <ButtonGroup
                    className="btn-group-toggle float-right"
                    data-toggle="buttons"
                  >
                    <Button
                      tag="label"
                      className={classNames("btn-simple", {
                        active: this.state.displayData === "uv",
                      })}
                      color="info"
                      id="0"
                      size="sm"
                      onClick={() => this.setDisplayData("uv")}
                    >
                      <input
                        defaultChecked
                        className="d-none"
                        name="options"
                        type="radio"
                      />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        UV/day
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
                        active: this.state.displayData === "uvmax",
                      })}
                      onClick={() => this.setDisplayData("uvmax")}
                    >
                      <input className="d-none" name="options" type="radio" />
                      <span className="d-none d-sm-block d-md-block d-lg-block d-xl-block">
                        UV Max/week
                      </span>
                      <span className="d-block d-sm-none">
                        <i className="tim-icons icon-gift-2" />
                      </span>
                    </Button>
                  </ButtonGroup>
                </Col>
              </Row>
            </CardHeader>
            <CardBody>
              <div className="chart-area">
                <Line data={uvChart} options={lineChartOptions} />
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>
    );
  }
}

export default LineChart;
