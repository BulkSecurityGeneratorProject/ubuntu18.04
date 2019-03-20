import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IDirectory } from 'app/shared/model/directory.model';
import { DirectoryService } from './directory.service';

@Component({
    selector: 'jhi-directory-update',
    templateUrl: './directory-update.component.html'
})
export class DirectoryUpdateComponent implements OnInit {
    directory: IDirectory;
    isSaving: boolean;
    timeStamp: string;

    constructor(protected directoryService: DirectoryService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ directory }) => {
            this.directory = directory;
            this.timeStamp = this.directory.timeStamp != null ? this.directory.timeStamp.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.directory.timeStamp = this.timeStamp != null ? moment(this.timeStamp, DATE_TIME_FORMAT) : null;
        if (this.directory.id !== undefined) {
            this.subscribeToSaveResponse(this.directoryService.update(this.directory));
        } else {
            this.subscribeToSaveResponse(this.directoryService.create(this.directory));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDirectory>>) {
        result.subscribe((res: HttpResponse<IDirectory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
