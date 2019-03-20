import { Moment } from 'moment';

export interface IDirectory {
    id?: number;
    name?: string;
    parent?: string;
    type?: string;
    isDirectory?: boolean;
    timeStamp?: Moment;
}

export class Directory implements IDirectory {
    constructor(
        public id?: number,
        public name?: string,
        public parent?: string,
        public type?: string,
        public isDirectory?: boolean,
        public timeStamp?: Moment
    ) {
        this.isDirectory = this.isDirectory || false;
    }
}
