/*!

=========================================================
* Black Dashboard React v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/black-dashboard-react
* Copyright 2020 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/black-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
// nodejs library that concatenates classes
import classNames from "classnames";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

// reactstrap components
import {
  Button,
  Collapse,
  Input,
  InputGroup,
  NavbarBrand,
  Navbar,
  Nav,
  Container,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter
} from "reactstrap";

let filtering = false;

class AdminNavbar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      collapseOpen: false,
      modalSearch: false,
      color: "navbar-transparent",
    };
  }
  componentDidMount() {
    window.addEventListener("resize", this.updateColor);
  }
  componentWillUnmount() {
    window.removeEventListener("resize", this.updateColor);
  }
  // function that adds color white/transparent to the navbar on resize (this is for the collapse)
  updateColor = () => {
    if (window.innerWidth < 993 && this.state.collapseOpen) {
      this.setState({
        color: "bg-white",
      });
    } else {
      this.setState({
        color: "navbar-transparent",
      });
    }
  };
  // this function opens and closes the collapse on small devices
  toggleCollapse = () => {
    if (this.state.collapseOpen) {
      this.setState({
        color: "navbar-transparent",
      });
    } else {
      this.setState({
        color: "bg-white",
      });
    }
    this.setState({
      filtering: false,
      pickedDate: false,
      collapseOpen: !this.state.collapseOpen,
    });
  };
  // this function is to open the Search modal
  toggleModalSearch = () => {
    this.setState({
      modalSearch: !this.state.modalSearch,
    });
  };

  setDate = (date) => {
    this.setState({
      pickedDate: date
    })
  }

  updateDate = () => {
    this.toggleModalSearch()

    filtering = true

    if (this.props.onChange) {
      this.props.onChange(this.state);
    }

  }

  removeFilter = () => {
    filtering = false;

    this.setState({
      pickedDate: null
    })

    if (this.props.onChange) {
      this.props.onChange({ pickedDate: null });
    }
  }

  render() {
    return (
      <>
        <Navbar
          className={classNames("navbar-absolute", this.state.color)}
          expand="lg"
        >
          <Container fluid>
            <div className="navbar-wrapper">
              <div
                className={classNames("navbar-toggle d-inline", {
                  toggled: this.props.sidebarOpened,
                })}
              >
                <button
                  className="navbar-toggler"
                  type="button"
                  onClick={this.props.toggleSidebar}
                >
                  <span className="navbar-toggler-bar bar1" />
                  <span className="navbar-toggler-bar bar2" />
                  <span className="navbar-toggler-bar bar3" />
                </button>
              </div>
              <NavbarBrand href="/" style={{fontWeight: "bold", fontSize: "x-large"}}>BEST SPORTS CLIMATE</NavbarBrand>
            </div>
            <button
              aria-expanded={false}
              aria-label="Toggle navigation"
              className="navbar-toggler"
              data-target="#navigation"
              data-toggle="collapse"
              id="navigation"
              type="button"
              onClick={this.toggleCollapse}
            >
              <span className="navbar-toggler-bar navbar-kebab" />
              <span className="navbar-toggler-bar navbar-kebab" />
              <span className="navbar-toggler-bar navbar-kebab" />
            </button>
            <Collapse navbar isOpen={this.state.collapseOpen}>
              <Nav className="ml-auto" navbar>
                <InputGroup className="search-bar">
                  <Button
                    color="link"
                    data-target="#searchModal"
                    data-toggle="modal"
                    id="search-button"
                    onClick={this.toggleModalSearch}
                    style={{color:"white"}}
                  >
                    Search specific date	&nbsp;	&nbsp;
                    <i className="tim-icons icon-zoom-split" />
                  </Button>
                  {filtering &&
                    <Button color="secondary"
                      onClick={this.removeFilter}
                    >
                      Remove filter
                    </Button>}
                </InputGroup>
                <li className="separator d-lg-none" />
              </Nav>
            </Collapse>
          </Container>
        </Navbar>
        <Modal
          isOpen={this.state.modalSearch}
          toggle={this.toggleModalSearch}
        >
          <ModalHeader  tag="h4" toggle={this.toggleModalSearch}>Choose a date</ModalHeader>
          <ModalBody style={{ paddingLeft: "25%" }}>
            <DatePicker
              selected={this.state.pickedDate ? this.state.pickedDate : new Date()}
              onChange={date => this.setDate(date)}
              minDate={new Date('2020-04-04')}
              maxDate={new Date()}
              inline
            />
          </ModalBody>
          <ModalFooter>
            <Button style={{marginLeft: "80%" }} color="secondary" onClick={this.updateDate}>Ok</Button>
          </ModalFooter>
        </Modal>
      </>
    );
  }
}

export default AdminNavbar;
