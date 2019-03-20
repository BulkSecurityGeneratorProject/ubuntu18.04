import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDirectory } from 'app/shared/model/directory.model';
import { DirectoryService } from './directory.service';

@Component({
    selector: 'jhi-directory-delete-dialog',
    templateUrl: './directory-delete-dialog.component.html'
})
export class DirectoryDeleteDialogComponent {
    directory: IDirectory;

    constructor(
        protected directoryService: DirectoryService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.directoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'directoryListModification',
                content: 'Deleted an directory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-directory-delete-popup',
    template: ''
})
export class DirectoryDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ directory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DirectoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.directory = directory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/directory', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/directory', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
