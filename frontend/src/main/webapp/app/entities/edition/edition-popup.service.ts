import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Edition } from './edition.model';
import { EditionService } from './edition.service';
@Injectable()
export class EditionPopupService {
    private isOpen = false;
    constructor (
        private modalService: NgbModal,
        private router: Router,
        private editionService: EditionService
    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.editionService.find(id).subscribe(edition => {
                if (edition.launchDate) {
                    edition.launchDate = {
                        year: edition.launchDate.getFullYear(),
                        month: edition.launchDate.getMonth() + 1,
                        day: edition.launchDate.getDate()
                    };
                }
                this.editionModalRef(component, edition);
            });
        } else {
            return this.editionModalRef(component, new Edition());
        }
    }

    editionModalRef(component: Component, edition: Edition): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.edition = edition;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
