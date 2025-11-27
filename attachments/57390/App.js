import logo from './logo.svg';
import './App.css';
import Home  from './Components/MyWebPage/Home.js';
import About from "./Components/MyWebPage/About"
import Contact from './Components/MyWebPage/Contact';
import  DisplayEmployeee  from './Components/DispalyEmployeee';
import DisplayEmp from './Components/DisplayEmp';
import Countchar from './Components/Countchar';
import Conditionrendering from './Components/Conditionrendering';
import ComponetLifecycle from './Components/ComponetLifecycle';
import Addemployee from './Components/Addemployee';
import Formusingformilk from './Components/Formusingformilk';
import ErrorBoundry from './Components/ErrorBoundry';
import Employee from './Components/Employee';
import IncreaseQty from './Components/IncreaseQty';
import Login from './Components/Login';
import Profile1 from './Components/Profile1';
import GetPost from './Components/GetPost';
import Submitpost from './Components/Submitpost';
import {Route, Routes,NavLink} from 'react-router-dom'
import {Nav, Navbar, Container} from 'react-bootstrap'
//import { ClassComp } from './Components/ClassComp';

function App() {
  return (
    <div className="Container">
      {/*  <img src={'/logo192.png'}></img>
     {/*<h1 className='bg-primary'>React App</h1>
     {/*<DisplayEmployeee id="101" name="peter" salary="4000"></DisplayEmployeee>
     {/* <ClassComp></ClassComp> 
     <DisplayEmp id="101" name="peter" salary="4000"></DisplayEmp>*/}
     {/*<Countchar></Countchar>*/}
     {/* <Conditionrendering></Conditionrendering>

     <ComponetLifecycle></ComponetLifecycle>
     <Addemployee></Addemployee> 
     
      <Formusingformilk></Formusingformilk>
      <ErrorBoundry>
      <Employee name="smith"></Employee>
      </ErrorBoundry>
      <ErrorBoundry>
      <Employee name="peter"><p>data</p></Employee>
      </ErrorBoundry>
      <ErrorBoundry>
      <Employee name="peter"><p>data</p></Employee>
      </ErrorBoundry>
        
         <IncreaseQty></IncreaseQty> 
         <Login></Login>
         <Profile1></Profile1>
         <GetPost></GetPost>
     <Submitpost></Submitpost>*/}
       <Navbar bg="dark" expand="lg" variant="dark">
  <Container>
    <Navbar.Brand href="#home">React</Navbar.Brand>
    <Navbar.Toggle aria-controls="basic-navbar-nav" />
    <Navbar.Collapse id="basic-navbar-nav">
      <Nav className="me-auto">
        <Nav.Link><NavLink to="/home" className="linkclass">Home</NavLink></Nav.Link>
        <Nav.Link><NavLink to="/about" className="linkclass">About</NavLink></Nav.Link>
        <Nav.Link><NavLink to="/contact" className="linkclass">Contact</NavLink></Nav.Link>
        <Nav.Link><NavLink to="/GetPosts" className="linkclass">Show Posts</NavLink></Nav.Link>

      </Nav>
    </Navbar.Collapse>
  </Container>
</Navbar>
    
      <Routes>
      <Route exact path='/' element={<Home />}></Route>
      <Route exact path='/home' element={<Home />}></Route>
      <Route exact path='/about' element={<About />}></Route>
      <Route exact path='/contact' element={<Contact />}></Route>  
      </Routes>
    </div>
  );
}

export default App;
