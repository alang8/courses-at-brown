export type Path = {[id:string]:number};

const PATH_LOCATION = "path"

export const StorePath = (setter: (path: Path) => void) => {
    return (path: Path) => {
        localStorage.setItem(PATH_LOCATION, JSON.stringify(path));
        setter(path);
    }
}

export const GetStoredPath = (): Path | undefined => {
    const stored = localStorage.getItem(PATH_LOCATION);
    return stored ? JSON.parse(stored) as Path : undefined;
}

export const ClearStoredPath = () => {
    localStorage.removeItem(PATH_LOCATION);
}