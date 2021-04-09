import React, {createRef, useEffect, useRef, useState} from "react"
import { Card, Container, Grid, GridRow, Header, Segment, Sticky } from "semantic-ui-react"
import CourseInfo from "../modules/CourseInfo";
import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';
import { User } from "../modules/Data"
import axios from "axios";
import 'reactjs-popup/dist/index.css';
import "../css/Graph.css"


const ClassGraph = (props) => {
    const fgRef = useRef();
    const [theNodes, setNodes] = useState([{}]);
    const [theCourses, setCourses] = useState([]);
    const [allCourseInfo, setAllCourseinfo] = useState({});
    const [gData, setGData] = useState({"nodes":[{}], "links":[{}]});

    let config = {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': '*',
        }
    }

    function getCourseData() {
        const toSend = {};

        let courseData = [];

        axios.post(
            'http://localhost:4567/getcourses',
            toSend,
            config
        ).then(response => {
            courseData = response.data['courses'];
            console.log("37")
            console.log(courseData)
            setCourses(courseData);
        }).catch(function (error) {
            console.log(error);
        });
    }

    function setupNodes() {
        let nodeArray= [];
        let linkArray = [];
        let i;
        let tempCourseInfo = {};

        console.log("course ln 47)")
        console.log(theCourses);
        for(i = 0; i < theCourses.length; i++) {
            let curID = theCourses[i]['id']
            let curName = theCourses[i]['name']
            let prereqInfo = theCourses[i]['prereqs']
            const courseCodeReg = /CSCI\s[0-9]{4}[A-Z]?/g;
            const prereqIDs = prereqInfo.match(courseCodeReg)
            console.log("prereq info");
            console.log(prereqInfo)
            console.log(prereqIDs)
            if (prereqIDs !== null) {
                let z;
                for (z = 0; z < prereqIDs.length; z++) {
                    linkArray.push({"source":prereqIDs[z], "target": curID});
                }
            }
            nodeArray.push({'id':curID, 'name':curID, 'val':8});
            tempCourseInfo[curID] = theCourses[i];
        }

        // let pathInfo = props.path;
        // let sem;
        // for(sem = 0; sem < pathInfo.length; sem++) {
        //
        // }

        setAllCourseinfo(tempCourseInfo);
        setGData({"nodes": nodeArray, "links":linkArray});
    }

    useEffect(() => {
        getCourseData();
        }, []);

    useEffect(() => {
        console.log("setup nodes");
        console.log(theCourses);
        setupNodes();
    }, [theCourses])

    const [open, setOpen] = useState(true);
    const [curCourse, setCurCourse] = useState({name:"DEFAULT", dept:"DEFAULT", code:"DEFAULT"});
    const closeModal = () => setOpen(false);

    useEffect(() => setOpen(!open), [curCourse])


    function displayedCourseInfo(nodeInfo) {
        let classID = nodeInfo['id'];
        console.log(classID)
        console.log(allCourseInfo[classID]);
        let rawCourse = allCourseInfo[classID]
        console.log("displ course info")
        console.log(rawCourse)
        let clickedCourse = {
            name:rawCourse['name'],
            dept:classID.substring(0,4),
            code:classID.substring(4),
            description:rawCourse['desc'],
            rating: rawCourse['crsrat'],
            latestProf: rawCourse['instr'],
            latestProfRating: rawCourse['profrat'],
            maxHours: rawCourse['maxhr'],
            avgHours: rawCourse['avghr']}
        setCurCourse(clickedCourse)

    }

    const forceRef = useRef(null);

    //Here we can fiddle with the forces, if we uncomment this line, the connected part looks nice but
    //disconnected part goes crazy.
    useEffect(() => {
        forceRef.current.d3Force("charge").strength(-20000);
        forceRef.current.d3Force("link").strength(0);
    });

    return <div>
                <div>
                    <ForceGraph2D
                        graphData={gData}
                        onNodeClick={(n, e) => {
                            displayedCourseInfo(n);
                        }}
                        ref={forceRef}
                        height={600}
                        width={1100}
                        showNavInfo = {true}
                        dagMode={"radialin"}
                    />
                </div>
                <CourseInfo course={curCourse} setDisplay={closeModal} shouldDisplay={open}/>
            </div>
}

export default ClassGraph;
