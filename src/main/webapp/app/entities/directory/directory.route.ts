import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Directory } from 'app/shared/model/directory.model';
import { DirectoryService } from './directory.service';
import { DirectoryComponent } from './directory.component';
import { DirectoryDetailComponent } from './directory-detail.component';
import { DirectoryUpdateComponent } from './directory-update.component';
import { DirectoryDeletePopupComponent } from './directory-delete-dialog.component';
import { IDirectory } from 'app/shared/model/directory.model';

@Injectable({ providedIn: 'root' })
export class DirectoryResolve implements Resolve<IDirectory> {
    constructor(private service: DirectoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDirectory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Directory>) => response.ok),
                map((directory: HttpResponse<Directory>) => directory.body)
            );
        }
        return of(new Directory());
    }
}

export const directoryRoute: Routes = [
    {
        path: '',
        component: DirectoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Directories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DirectoryDetailComponent,
        resolve: {
            directory: DirectoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Directories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DirectoryUpdateComponent,
        resolve: {
            directory: DirectoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Directories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DirectoryUpdateComponent,
        resolve: {
            directory: DirectoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Directories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const directoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DirectoryDeletePopupComponent,
        resolve: {
            directory: DirectoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Directories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
