import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDirectory } from 'app/shared/model/directory.model';
import { AccountService } from 'app/core';
import { DirectoryService } from './directory.service';

@Component({
    selector: 'jhi-directory',
    templateUrl: './directory.component.html'
})
export class DirectoryComponent implements OnInit, OnDestroy {
    directories: IDirectory[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected directoryService: DirectoryService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.directoryService
            .query()
            .pipe(
                filter((res: HttpResponse<IDirectory[]>) => res.ok),
                map((res: HttpResponse<IDirectory[]>) => res.body)
            )
            .subscribe(
                (res: IDirectory[]) => {
                    this.directories = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDirectories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDirectory) {
        return item.id;
    }

    registerChangeInDirectories() {
        this.eventSubscriber = this.eventManager.subscribe('directoryListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
