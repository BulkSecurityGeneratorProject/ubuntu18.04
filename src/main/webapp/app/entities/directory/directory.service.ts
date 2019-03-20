import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDirectory } from 'app/shared/model/directory.model';

type EntityResponseType = HttpResponse<IDirectory>;
type EntityArrayResponseType = HttpResponse<IDirectory[]>;

@Injectable({ providedIn: 'root' })
export class DirectoryService {
    public resourceUrl = SERVER_API_URL + 'api/directories';

    constructor(protected http: HttpClient) {}

    create(directory: IDirectory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(directory);
        return this.http
            .post<IDirectory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(directory: IDirectory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(directory);
        return this.http
            .put<IDirectory>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDirectory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDirectory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(directory: IDirectory): IDirectory {
        const copy: IDirectory = Object.assign({}, directory, {
            timeStamp: directory.timeStamp != null && directory.timeStamp.isValid() ? directory.timeStamp.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timeStamp = res.body.timeStamp != null ? moment(res.body.timeStamp) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((directory: IDirectory) => {
                directory.timeStamp = directory.timeStamp != null ? moment(directory.timeStamp) : null;
            });
        }
        return res;
    }
}
