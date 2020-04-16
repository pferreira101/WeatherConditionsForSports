import React from "react";

// reactstrap components
import {
    Card,
    CardHeader,
    CardBody,
    CardTitle,
    Col,
    Table,
} from "reactstrap";


class WeatherTable extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            labels: this.props.labels,
            conditionData: this.props.conditionData,
            humidityData: this.props.humidityData,
            windData: this.props.windData
        };

    }
    render() {
        return (
            <Col md="12">
                <Card>
                    <CardHeader>
                        <CardTitle tag="h4">Simple Table</CardTitle>
                    </CardHeader>
                    <CardBody>
                        <Table className="tablesorter" responsive>
                            <thead className="text-primary">
                                <tr>
                                    <th></th>
                                    {this.state.labels.map((label) => <th className="text-secondary"> {label}</th>)}
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th  className="text-secondary" scope="row">Condition</th>
                                    {this.state.conditionData.map((condition) => <td className="text-center">{condition}</td>)}
                                </tr>
                                <tr>
                                    <th className="text-secondary" scope="row">Humidity</th>
                                    {this.state.humidityData.map((humidity) => <td className="text-center">{humidity}</td>)}
                                </tr>
                                <tr>
                                    <th className="text-secondary" scope="row">Wind</th>
                                    {this.state.windData.map((wind) => <td className="text-center">{wind}</td>)}
                                </tr>
                            </tbody>
                        </Table>
                    </CardBody>
                </Card>
            </Col>
        );
    }
}

export default WeatherTable;
