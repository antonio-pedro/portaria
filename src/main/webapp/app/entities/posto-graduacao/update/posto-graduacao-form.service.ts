import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPostoGraduacao, NewPostoGraduacao } from '../posto-graduacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPostoGraduacao for edit and NewPostoGraduacaoFormGroupInput for create.
 */
type PostoGraduacaoFormGroupInput = IPostoGraduacao | PartialWithRequiredKeyOf<NewPostoGraduacao>;

type PostoGraduacaoFormDefaults = Pick<NewPostoGraduacao, 'id'>;

type PostoGraduacaoFormGroupContent = {
  id: FormControl<IPostoGraduacao['id'] | NewPostoGraduacao['id']>;
  descricao: FormControl<IPostoGraduacao['descricao']>;
  sigla: FormControl<IPostoGraduacao['sigla']>;
};

export type PostoGraduacaoFormGroup = FormGroup<PostoGraduacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PostoGraduacaoFormService {
  createPostoGraduacaoFormGroup(postoGraduacao: PostoGraduacaoFormGroupInput = { id: null }): PostoGraduacaoFormGroup {
    const postoGraduacaoRawValue = {
      ...this.getFormDefaults(),
      ...postoGraduacao,
    };
    return new FormGroup<PostoGraduacaoFormGroupContent>({
      id: new FormControl(
        { value: postoGraduacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descricao: new FormControl(postoGraduacaoRawValue.descricao, {
        validators: [Validators.required],
      }),
      sigla: new FormControl(postoGraduacaoRawValue.sigla, {
        validators: [Validators.required],
      }),
    });
  }

  getPostoGraduacao(form: PostoGraduacaoFormGroup): IPostoGraduacao | NewPostoGraduacao {
    return form.getRawValue() as IPostoGraduacao | NewPostoGraduacao;
  }

  resetForm(form: PostoGraduacaoFormGroup, postoGraduacao: PostoGraduacaoFormGroupInput): void {
    const postoGraduacaoRawValue = { ...this.getFormDefaults(), ...postoGraduacao };
    form.reset(
      {
        ...postoGraduacaoRawValue,
        id: { value: postoGraduacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PostoGraduacaoFormDefaults {
    return {
      id: null,
    };
  }
}
