IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fee_findall')
		  )
  DROP PROCEDURE fee_findall
GO

CREATE PROCEDURE fee_findall
          @pageNumber  INT =1,
          @pageSize INT = 10,
		  @searchByName nvarchar(250) =null,
		  @merchantId int = null
      AS

       DECLARE @cnt INT
       DECLARE @offset INT

              SELECT COUNT(*) AS cnt FROM fees_settings (NOLOCK)
			  WHERE
			   	(merchant_id = @merchantId or @merchantId is null) and
				([name] like '%' + @searchByName + '%' or @searchByName is null)

               SET @pageNumber = ABS(@pageNumber)
               SET @pageSize = ABS(@pageSize)
               IF @pageNumber < 1
                SET @pageNumber = 1
               IF @pageSize < 1
                SET @pageSize = 1

               SET @offset = (@pageNumber - 1) * @pageSize

               SELECT * FROM fees_settings (NOLOCK)
			   WHERE
					(merchant_id = @merchantId or @merchantId is null) and
					([name] like '%' + @searchByName + '%' or @searchByName is null)

               ORDER BY id OFFSET @offset ROWS FETCH NEXT @pageSize ROWS ONLY


GO

IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fee_insertupdate')
		  )
  DROP PROCEDURE fee_insertupdate
GO

IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fee_insertupdate')
		  )
  DROP PROCEDURE fee_insertupdate
GO

CREATE PROCEDURE [dbo].fee_insertupdate
	@Id smallint = 0,
	@Name nvarchar(255) = NULL,
	@Value decimal(10, 2),
	@IsPercentage bit = 0,
	@Description nvarchar(255) = NULL,
	@MerchantId int =null,

	@modifiedBy nvarchar(255),
	@createdBy nvarchar(255),
	@dateCreated  datetime = getdate,
	@dateModified datetime = getdate
AS

IF EXISTS(SELECT [Id] FROM [dbo].fees_settings (NOLOCK) WHERE [Id] = @Id OR [Name] = @Name and merchant_id = @MerchantId)
BEGIN
	UPDATE [dbo].fees_settings SET
		[Name] = @Name,
		[Value] = @Value,
		[Description] = @Description,
		[merchant_id] = @MerchantId,
		[is_percentage] = @IsPercentage,
		--Audit Properties --
		modified_by = @modifiedBy,
		date_modified = @dateModified
	WHERE
		([Id] = @Id OR [Name] = @Name) and merchant_id = @MerchantId

	 SELECT @Id = app.id FROM [dbo].[fees_settings] app (nolock)
	 WHERE  ([Id] = @Id OR [Name] = @Name) and [merchant_id] = @MerchantId
RETURN @Id
END
ELSE
BEGIN

INSERT INTO [dbo].[fees_settings](
	[Name],
	[Value],
	[Description],
	[merchant_id],
	[is_percentage],

	[modified_by],
	[created_by],
	[date_created],
	[date_modified]
) VALUES (
	@Name,
	@Value,
	@Description,
	@MerchantId,
	@IsPercentage,

	@modifiedBy,
	@createdBy,
	@dateCreated,
	@dateModified
)

SET @Id = SCOPE_IDENTITY()
SELECT @Id

RETURN @Id

END
 --- end insert


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'delete_fee_byid')
		  )
  DROP PROCEDURE delete_fee_byid

GO
CREATE PROCEDURE [dbo].[delete_fee_byid]
	@Id smallint,
	@MerchantId int
AS

DELETE [dbo].[fees_settings] WHERE [Id] = @Id and merchant_id =  @MerchantId



GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fee_getbyid')
		  )
  DROP PROCEDURE [fee_getbyid]
GO

CREATE PROCEDURE [dbo].[fee_getbyid]
	@Id smallint,
	@merchantId int
AS

SELECT top 1* FROM [dbo].[fees_settings] (nolock)
WHERE
	[Id] = @Id and (merchant_id = @merchantId or @merchantId is null)


GO
IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fee_getbyname'))
  DROP PROCEDURE fee_getbyname
GO

CREATE PROCEDURE [dbo].[fee_getbyname]
	@name nvarchar(250),
	@merchantId int
AS

SELECT top 1 * FROM [dbo].[fees_settings] (nolock)
WHERE [Name] = @name and (merchant_id = @merchantId or @merchantId is null)