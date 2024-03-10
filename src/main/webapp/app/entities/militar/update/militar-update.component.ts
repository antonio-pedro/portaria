import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPostoGraduacao } from 'app/entities/posto-graduacao/posto-graduacao.model';
import { PostoGraduacaoService } from 'app/entities/posto-graduacao/service/posto-graduacao.service';
import { IOM } from 'app/entities/om/om.model';
import { OMService } from 'app/entities/om/service/om.service';
import { MilitarService } from '../service/militar.service';
import { IMilitar } from '../militar.model';
import { MilitarFormService, MilitarFormGroup } from './militar-form.service';

@Component({
  standalone: true,
  selector: 'jhi-militar-update',
  templateUrl: './militar-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MilitarUpdateComponent implements OnInit {
  isSaving = false;
  militar: IMilitar | null = null;

  postoGraduacaosSharedCollection: IPostoGraduacao[] = [];
  oMSSharedCollection: IOM[] = [];

  editForm: MilitarFormGroup = this.militarFormService.createMilitarFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected militarService: MilitarService,
    protected militarFormService: MilitarFormService,
    protected postoGraduacaoService: PostoGraduacaoService,
    protected oMService: OMService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePostoGraduacao = (o1: IPostoGraduacao | null, o2: IPostoGraduacao | null): boolean =>
    this.postoGraduacaoService.comparePostoGraduacao(o1, o2);

  compareOM = (o1: IOM | null, o2: IOM | null): boolean => this.oMService.compareOM(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ militar }) => {
      this.militar = militar;
      if (militar) {
        this.updateForm(militar);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('portariaApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const militar = this.militarFormService.getMilitar(this.editForm);
    if (militar.id !== null) {
      this.subscribeToSaveResponse(this.militarService.update(militar));
    } else {
      this.subscribeToSaveResponse(this.militarService.create(militar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMilitar>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(militar: IMilitar): void {
    this.militar = militar;
    this.militarFormService.resetForm(this.editForm, militar);

    this.postoGraduacaosSharedCollection = this.postoGraduacaoService.addPostoGraduacaoToCollectionIfMissing<IPostoGraduacao>(
      this.postoGraduacaosSharedCollection,
      militar.posto,
    );
    this.oMSSharedCollection = this.oMService.addOMToCollectionIfMissing<IOM>(this.oMSSharedCollection, militar.om);
  }

  protected loadRelationshipsOptions(): void {
    this.postoGraduacaoService
      .query()
      .pipe(map((res: HttpResponse<IPostoGraduacao[]>) => res.body ?? []))
      .pipe(
        map((postoGraduacaos: IPostoGraduacao[]) =>
          this.postoGraduacaoService.addPostoGraduacaoToCollectionIfMissing<IPostoGraduacao>(postoGraduacaos, this.militar?.posto),
        ),
      )
      .subscribe((postoGraduacaos: IPostoGraduacao[]) => (this.postoGraduacaosSharedCollection = postoGraduacaos));

    this.oMService
      .query()
      .pipe(map((res: HttpResponse<IOM[]>) => res.body ?? []))
      .pipe(map((oMS: IOM[]) => this.oMService.addOMToCollectionIfMissing<IOM>(oMS, this.militar?.om)))
      .subscribe((oMS: IOM[]) => (this.oMSSharedCollection = oMS));
  }
}
