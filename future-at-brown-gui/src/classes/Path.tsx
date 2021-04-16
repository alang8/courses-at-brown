//Type which corresponds to a concentration path - the course id : semester number (0=Fall 2021, 1=Spring 2022, etc)
export type Path = {[id:string]:number};

const PATH_LOCATION = "path"

//Function to store the path for a given user.
export const StorePath = (setter: (path: Path) => void) => {
    return (path: Path) => {
        localStorage.setItem(PATH_LOCATION, JSON.stringify(path));
        setter(path);
    }
}

//Function to get the stored path for displaying on the graph.
export const GetStoredPath = (): Path | undefined => {
    const stored = localStorage.getItem(PATH_LOCATION);
    return stored ? JSON.parse(stored) as Path : undefined;
}

//Function to clear the path thats stored in memory
export const ClearStoredPath = () => {
    localStorage.removeItem(PATH_LOCATION);
}