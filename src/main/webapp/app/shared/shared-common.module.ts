import { NgModule } from '@angular/core';

import { Ubuntu18SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [Ubuntu18SharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [Ubuntu18SharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class Ubuntu18SharedCommonModule {}
