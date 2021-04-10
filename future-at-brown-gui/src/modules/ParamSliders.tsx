import React from "react";
import { Button, Dimmer, Header, Loader, Segment } from "semantic-ui-react";
import { defaultParams, SearchParams, User } from "./Data";
import { useState } from "react";
import Slider, { Range } from 'rc-slider';
import 'rc-slider/assets/index.css';

interface Params {
    curUser?: User;
    prefChange?: (newPref: SearchParams) => Promise<void>;
}

const ParamSlider: React.FC<Params> = (props) => {

    const [pref, setPrefs] = useState<SearchParams>(props.curUser?.preferences ?? defaultParams);
    const [isLoading, setLoading] = useState<boolean>(true);

    const makeSlider = (
        color: string,
        name: string,
        id: "avgHoursPref" | "crsRatingPref" | "maxHoursPref" | "crsSizePref" | "profRatingPref"
    ): JSX.Element => {
        return (
            <div style={{ padding: '1rem', display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
                <div style={{ width: '100%' }}>
                    <Header as="h5">
                        {name + ": "}
                        <em style={{color: color}}>
                            {pref[id]}
                        </em>
                    </Header>
                </div>
                <div style={{ width: '100%' }}>
                    <Slider
                        min={0}
                        max={10}
                        defaultValue={5}
                        step={0.1}
                        trackStyle={{ backgroundColor: color }}
                        handleStyle={{ borderColor: color }}
                        dotStyle={{ borderColor: color }}
                        onChange={(newVal: number) => {
                            pref[id] = newVal;
                            setPrefs({ ...pref });
                        }}
                        onAfterChange={(newVal: number) => {
                            if (props.prefChange) {
                                setLoading(true)
                                pref[id] = newVal;
                                props.prefChange!({...pref})
                                    .then(() => setLoading(false));
                            }
                            
                        }}
                        marks={[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}
                    />
                </div>

            </div>
        )
    }

    return (
        <div className={"sliders"}>
            {makeSlider("red", "Average Hours", "avgHoursPref")}
            {makeSlider("goldenRod", "Max Hours", "maxHoursPref")}
            {makeSlider("springGreen", "Class Rating", "crsRatingPref")}
            {makeSlider("deepSkyBlue", "Professor Rating", "profRatingPref")}
            {makeSlider("fuchsia", "Class Size", "crsSizePref")}
            <div style={{height: "1rem"}} />
        </div>
    );
}

// const setParams = async (newParams: SearchParams) => {
//     // TODO: add something that will set the search paramaeters
//     await new Promise(resolve => setTimeout(resolve, 2000));
// }

export default ParamSlider