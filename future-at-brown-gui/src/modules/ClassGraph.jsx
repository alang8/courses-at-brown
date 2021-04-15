import { useEffect, useRef, useState } from "react"
import CourseInfo from "../modules/CourseInfo";
import { ForceGraph2D } from 'react-force-graph';
import axios from "axios";
import "../css/Graph.css"
import { GetColorRaw } from '../classes/Colors'
import { GetCode } from '../classes/Course'

/**
 * React component which deals with the actual graph display itself.
 * @param props - the props for the component: 'path': {course codes:semester number}, 'saveFunction':function to save course
 * @returns {JSX.Element} - an html object which holds the ForceGraph2D.
 */
const ClassGraph = (props) => {
    const fgRef = useRef();
    const [theCourses, setCourses] = useState([]);
    const [allCourseInfo, setAllCourseinfo] = useState({});
    const [gData, setGData] = useState({ "nodes": [], "links": [] });

    let theSemester = { 0: "Fall 2021", 1: "Spring 2022", 2: "Fall 2022", 3: "Spring 2023", 4: "Fall 2023", 5: "Spring 2024", 6: "Fall 2024", 7: "Spring 2025", 8: "Fall 2025", 9: "Spring 2026" }

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
        let nodeArray = [];
        let linkArray = [];
        let i;
        let tempCourseInfo = {};
        const thePath = [];

        console.log(theCourses);
        for (i = 0; i < theCourses.length; i++) {
            let curID = theCourses[i]['id']
            let prereqInfo = theCourses[i]['prereqs']
            const courseCodeReg = /[A-Z]{4}\s[0-9]{4}[A-Z]?/g;
            const prereqIDs = prereqInfo.match(courseCodeReg)
            if (prereqIDs !== null) {
                let z;
                for (z = 0; z < prereqIDs.length; z++) {
                    linkArray.push({ "source": prereqIDs[z], "target": curID });
                }
            }
            // let val = 8;
            // if (curID in props.path) {
            //     val = 100;
            // }
            nodeArray.push({ 'id': curID, 'name': curID });
            tempCourseInfo[curID] = theCourses[i];
        }

        for (i = 0; i < 9; i++) {
            const dests = [];
            const origs = [];
            for (let code in props.path) {
                if (props.path[code] === i)
                    origs.push(code);
                else if (props.path[code] === i + 1) {
                    dests.push(code);
                }

            }
            origs.forEach((o) =>
                dests.forEach((d) =>
                    thePath.push({ "source": o, "target": d })));
        }
        setAllCourseinfo(tempCourseInfo);
        setGData({ "nodes": nodeArray, "links": linkArray.concat(thePath) });
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
    const [curCourse, setCurCourse] = useState({ name: "DEFAULT", dept: "CSCI", code: "DEFAULT" });
    const closeModal = () => setOpen(false);

    useEffect(() => setOpen(!open), [curCourse])

    /**
     * Function to retrieve the relevant data for the clicked course popup.
     * @param nodeInfo - the information of the clicked node (basically just the dept + code as a string).
     */
    function displayedCourseInfo(nodeInfo) {
        console.log("user", props.user.getSaved())
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
            name: rawCourse['name'],
            dept: classID.substring(0, 4),
            code: classID.substring(4),
            description: rawCourse['desc'],
            rating: rawCourse['crsrat'],
            latestProf: rawCourse['instr'],
            latestProfRating: rawCourse['profrat'],
            maxHours: rawCourse['maxhr'],
            avgHours: rawCourse['avghr'],
            prereqs: (prereqText === "" ? "None Listed" : prereqText),
        }
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
    function nodePaint({ id, x, y }, ctx) {
        if (id in props.path) {
            // ctx.fillStyle = GetColorRaw(id.substring(0, 4));
            // ctx.strokeStyle = GetColorRaw(id.substring(0, 4));
            ctx.beginPath();
            // ctx.arc(x, y, 100, 0, 2 * Math.PI, false);
            // ctx.fill();
            ctx.fillStyle = "white"
            ctx.font = 'bold 24px Crimson Text Times New Roman serif';
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillText(id, x, y - 15);
            ctx.fillText(theSemester[props.path[id]], x, y + 9);
            ctx.stroke();
        } else {
            ctx.fillStyle = "white";
            ctx.beginPath();
            ctx.arc(x, y, 13, 0, 2 * Math.PI, false);
            ctx.fill();
        }
        //
        //     if (id) {
        //         console.log("undefined node")
        //     } else {
        //         console.log("oop")
        //     }
        //     // ctx.strokeStyle = GetColorRaw(id.substring(0, 4));
        //     ctx.beginPath();
        //     ctx.arc(x, y, 20, 0, 2 * Math.PI, false);
        //     ctx.fill();
        // }
    }

    // function edgePaint()

    //Changing the force values to display our graph in a reasonable way.
    useEffect(() => {
        fgRef.current.d3Force("charge").strength(-20000);
        fgRef.current.d3Force("link").strength(0.10);
    });

    function nodeInPath(inPath, notInPath) {
        return (node) => (node.id in props.path) ? inPath : notInPath
    }

    function edgeInPath(inPath, notInPath) {
        return (edge) => (edge.source.id in props.path && edge.target.id in props.path) ? inPath : notInPath
    }

    function edgeColor(edge) {
        return (edge.source.id in props.path && edge.target.id in props.path) ?
            "black" : GetColorRaw(edge.source.id?.substring(0, 4) ?? "CSCI");
    }

    function edgeLabel(edge) {
        return (edge.source.id in props.path && edge.target.id in props.path) ?
            theSemester[props.path[edge.source.id]] + " > " + theSemester[props.path[edge.target.id]]
            : undefined
    }

    return <div>
        <div id="graphWrapper">
            <ForceGraph2D
                graphData={gData}
                onNodeClick={(n, e) => displayedCourseInfo(n) }
                ref={fgRef}
                showNavInfo={true}
                dagMode={"radialout"}
                nodeVal={nodeInPath(600, 20)}
                nodeColor={node => GetColorRaw(node.id?.substring(0, 4) ?? "CSCI")}
                linkColor={edgeColor}
                linkWidth={edgeInPath(2, 1)}
                linkCurvature={edgeInPath(0.3, 0)}
                linkDirectionalParticles={edgeInPath(5, 1)}
                linkDirectionalParticleWidth={edgeInPath(8, 4)}
                linkDirectionalArrowLength={edgeInPath(30, 0)}
                linkDirectionalArrowRelPos={1}
                linkLabel={edgeLabel}
                nodeCanvasObject={(node, ctx) => nodePaint(node, ctx)}
                nodeCanvasObjectMode={() => "after"}
            />
        </div>
        <CourseInfo
            course={curCourse}
            setDisplay={closeModal}
            shouldDisplay={open}
            button={{ func: props.saveFunction }}
            shouldDisable={(test) => (props.user.getSaved().find(c => GetCode(c) === GetCode(test)) !== undefined)} />
    </div>
}

export default ClassGraph;