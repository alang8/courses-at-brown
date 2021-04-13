import {useEffect, useRef, useState} from "react"
import CourseInfo from "../modules/CourseInfo";
import CourseTile from "../modules/CourseTile";
import { ForceGraph2D } from 'react-force-graph';
import axios from "axios";
import "../css/Graph.css"
import {GetColorRaw} from '../classes/Colors'

/**
 * React component which deals with the actual graph display itself.
 * @param props - the props for the component: 'path': {course codes:semester number}, 'saveFunction':function to save course
 * @returns {JSX.Element} - an html object which holds the ForceGraph2D.
 */
const ClassGraph = (props) => {
    const fgRef = useRef();
    const [theNodes, setNodes] = useState([{}]);
    const [theCourses, setCourses] = useState([]);
    const [allCourseInfo, setAllCourseinfo] = useState({});
    const [gData, setGData] = useState({"nodes":[{}], "links":[{}]});

    //TEST PATH
    // let thePath = {"CSCI 0170":0, "CSCI 0220":1, "CSCI 0180":1, "CSCI 0330":2, "CSCI 1470":2, "CSCI 0320":3, "APMA 0360":5}
    let theSemester = {0:"Fall 2021", 1:"Spring 2022", 2:"Fall 2022", 3:"Spring 2023", 4:"Fall 2023", 5:"Spring 2024", 6:"Fall 2024", 7:"Spring 2025", 8:"Fall 2025", 9:"Spring 2026"}

    //Config for axios.
    let config = {
        headers: {
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': '*',
        }
    }

    /**
     * Function to get all the course data to display on the graph.
     */
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

    /**
     * Function to setup the node and link array for the ForceGraph component.
     */
    function setupNodes() {
        console.log("in classGraph")
        console.log(props.path)
        let nodeArray= [];
        let linkArray = [];
        let i;
        let tempCourseInfo = {};

        console.log(theCourses);
        for(i = 0; i < theCourses.length; i++) {
            let curID = theCourses[i]['id']
            let prereqInfo = theCourses[i]['prereqs']
            const courseCodeReg = /[A-Z]{4}\s[0-9]{4}[A-Z]?/g;
            const prereqIDs = prereqInfo.match(courseCodeReg)
            if (prereqIDs !== null) {
                let z;
                for (z = 0; z < prereqIDs.length; z++) {
                    linkArray.push({"source":prereqIDs[z], "target": curID});
                }
            }
            let val = 8;
            if (curID in props.path) {
                val = 100;
            }
            nodeArray.push({'id':curID, 'name':curID, 'val':val});
            tempCourseInfo[curID] = theCourses[i];
        }

        setAllCourseinfo(tempCourseInfo);
        setGData({"nodes": nodeArray, "links":linkArray});
    }

    //Want to get all course data upon load
    useEffect(() => {
        getCourseData();
    }, []);

    //Want to setup the nodes once we recieved all the course info.
    useEffect(() => {
        console.log("setup nodes");
        console.log(theCourses);
        setupNodes();
    }, [theCourses])

    //State vars for the popup window when clicking on a specific node.
    const [open, setOpen] = useState(true);
    const [curCourse, setCurCourse] = useState({name:"DEFAULT", dept:"DEFAULT", code:"DEFAULT"});
    const closeModal = () => setOpen(false);

    useEffect(() => setOpen(!open), [curCourse])


    /**
     * Function to retrieve the relevant data for the clicked course popup.
     * @param nodeInfo - the information of the clicked node (basically just the dept + code as a string).
     */
    function displayedCourseInfo(nodeInfo) {
        let classID = nodeInfo['id'];
        console.log(classID)
        console.log(allCourseInfo[classID]);
        let rawCourse = allCourseInfo[classID]
        console.log("displ course info")
        console.log(rawCourse)
        let encodedPrereq = rawCourse['prereqs'];
        let prereqText = encodedPrereq.replaceAll("&", " and ")
        prereqText = prereqText.replaceAll("|", " or ")
        let clickedCourse = {
            name:rawCourse['name'],
            dept:classID.substring(0,4),
            code:classID.substring(4),
            description:rawCourse['desc'],
            rating: rawCourse['crsrat'],
            latestProf: rawCourse['instr'],
            latestProfRating: rawCourse['profrat'],
            maxHours: rawCourse['maxhr'],
            avgHours: rawCourse['avghr'],
            prereqs: (prereqText === "" ? "None Listed": prereqText)}
        setCurCourse(clickedCourse)

    }

    /**
     * Function to paint our nodes (painting ones in our path differently from others).
     * @param id - the id of the node.
     * @param x - the x coordinate of the node to draw.
     * @param y - the y coordinate of the node to draw.
     * @param color - the color of the node (based on course department).
     * @param ctx - the context in which we will draw the node.
     */
    function nodePaint({ id, x, y }, color, ctx) {
        if(id in props.path) {
            ctx.fillStyle = GetColorRaw(id.substring(0,4));
            ctx.beginPath();
            ctx.arc(x, y, 100, 0, 2 * Math.PI, false);
            ctx.fill();
            ctx.fillStyle = "white"
            ctx.font = '24px Sans-Serif';
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillText(id, x, y-15);
            ctx.fillText(theSemester[props.path[id]], x, y + 9);
            ctx.stroke();
        } else {
            ctx.fillStyle = "#b1c2de";
            ctx.beginPath();
            ctx.arc(x, y, 15, 0, 2 * Math.PI, false);
            ctx.fill();
        }
    }

    //Changing the force values to display our graph in a reasonable way.
    useEffect(() => {
        fgRef.current.d3Force("charge").strength(-15000);
        fgRef.current.d3Force("link").strength(0);
    });

    return <div>
        <div id="graphWrapper">
            <ForceGraph2D
                graphData={gData}
                onNodeClick={(n, e) => {
                    displayedCourseInfo(n);
                }}
                ref={fgRef}
                showNavInfo = {true}
                dagMode={"radialin"}
                dagLevelDistance={100}
                height={600}
                width={1125}
                nodeCanvasObject={(node, ctx) => nodePaint(node, "black", ctx)}
            />
        </div>
        <CourseInfo course={curCourse} setDisplay={closeModal} shouldDisplay={open} button={{func: props.saveFunction}}/>
    </div>
}

export default ClassGraph;
