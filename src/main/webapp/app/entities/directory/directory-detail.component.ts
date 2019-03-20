import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDirectory } from 'app/shared/model/directory.model';

@Component({
    selector: 'jhi-directory-detail',
    templateUrl: './directory-detail.component.html'
})
export class DirectoryDetailComponent implements OnInit {
    directory: IDirectory;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ directory }) => {
            this.directory = directory;
        });
    }

    previousState() {
        window.history.back();
    }
}
